;; clj src/scripts/clojure/nzqr/scripts/plots.clj
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns nzqr.scripts.plots
  
  {:doc "data files for R plots of interpolators"
   :author "palisades dot lakes at gmail dot com"
   :version "2018-10-09"}
  
  (:require [clojure.java.io :as io]
            [clojure.string :as s])
  (:import 
    [java.io PrintWriter]
    [java.lang Math]
    [java.lang.reflect Method]
    [java.util Arrays]
    [nzqr.java.functions Doubles]
    [nzqr.java.functions.scalar 
     ClosedInterval Interval 
     ConstantFunctional AffineFunctional QuadraticLagrange 
     QuadraticMonomial QuadraticMonomialShifted 
     QuadraticMonomialStandardized QuadraticNewton
     CubicHermite CubicLagrange CubicMonomial CubicNewton
     ScalarFunctional]
    [nzqr.java.test.scalar Common]))

;;----------------------------------------------------------------

(def interpolators
  [#_ConstantFunctional
   #_AffineFunctional
   QuadraticLagrange QuadraticMonomial QuadraticMonomialShifted
   QuadraticMonomialStandardized QuadraticNewton
   CubicHermite CubicLagrange CubicMonomial CubicNewton])

;;----------------------------------------------------------------

(defn- valid-knots? [^Class interpolator ^"[[D" knots]
  (let [^Method method (.getMethod interpolator "validKnots"
                         (into-array Class [(Class/forName "[[D")]))]
    #_(println (.toString method))
    (boolean
      (.invoke method nil (into-array Object [knots])))))

;;----------------------------------------------------------------

(defn- ^ScalarFunctional interpolate [^Class interpolator 
                                      ^ScalarFunctional f 
                                      ^"[[D" knots]
  
  "Return an instance of <code>interpolator</code>
   constructed by call the 'interpolate' class (static) method on
   the supplied test function and knots."
  
  (let [^Method method (.getMethod interpolator "interpolate"
                         (into-array Class [Object Object]))]
    (.invoke method nil (into-array Object [f knots]))))

;;----------------------------------------------------------------

(defn prefix [^ScalarFunctional testf
              ^"[[D" knots]
  (s/join "." [(.safeName testf) (Common/knotString knots)]))

;;----------------------------------------------------------------
;; Either "valueKnot" or "slopeKnot".

(defn- append-knots [^String kind 
                     ^ScalarFunctional f
                     ^doubles knots 
                     ^PrintWriter w]
  (dotimes [i (alength knots)]
    (let [x (double (aget knots i))]
      (.write w (s/join "\t" [kind x (.doubleValue f x)]))
      (.write w "\n"))))

;;----------------------------------------------------------------

(defn- append-xfx 
  ([^String fname ^ScalarFunctional f xs ^PrintWriter w]
    (doseq [x xs]
      (let [x (double x)]
        (.write w (s/join "\t" [fname x (.doubleValue f x)]))
        (.write w "\n"))))
  ([^ScalarFunctional f xs ^PrintWriter w]
    (append-xfx (.safeName f) f xs w)))
;;----------------------------------------------------------------
;; return a sequence of n equally spaced values that go 
;; approximately from xmin to xmax

(defn- xcover [^Interval support ^long n]
  (let [xmin (.lower support)
        xmax (.upper support)
        dx (/ (- xmax xmin) (dec n))
        step (fn ^double [^double x] (+ x dx))]
    (filter #(.contains support (double %))
            (take n (iterate step (- xmin dx))))))

;;----------------------------------------------------------------

(defn- write-macro-file [^ScalarFunctional testf
                         ^"[[D" knots 
                         interpolants]
  (when (and (Double/isFinite (.doubleArgmin testf Interval/ALL))
             (not (empty? interpolants)))
    (println testf)
    (let [file (io/file 
                 "data" "interpolate" "macro"
                 (s/join "." [(prefix testf knots) 
                              "macro" "tsv"]))
          _(println (.getPath file))
          _(io/make-parents file)
          _(println (Arrays/toString ^doubles (aget knots 0)))
          _(println (Arrays/toString ^doubles (aget knots 1)))
          argmin (.doubleArgmin testf Interval/ALL)
          ^Interval support (.expand
                              (.cover (ClosedInterval/make 
                                        (double (Doubles/min knots))
                                        (double (Doubles/max knots)))
                                argmin)
                              0.5)
          xs (xcover support (long 511))]
      (with-open [^PrintWriter w (PrintWriter. (io/writer file))]
        (.write w (s/join "\t" ["functional" "x" "y"]))
        (.write w "\n")
        (append-knots "argmin" testf (double-array 1 argmin) w)
        (append-knots "valueKnot" testf (aget knots 0) w)
        (append-knots "slopeKnot" testf (aget knots 1) w)
        (append-xfx "testf" testf xs w)
        (doseq [^ScalarFunctional interpolant interpolants]
          (append-xfx interpolant xs w))))))

;;----------------------------------------------------------------
;; return the result of calling (Math/nextDown .) n times,
;; starting from x

(defn- next-down [^double x ^long n]
  (loop [i 0
         x x]
    (if (< i n)
      (recur (inc i) (Math/nextDown x))
      x)))

;;----------------------------------------------------------------
;; return the sequence of values resulting from calling 
;; (Math/nextUp x) n times, starting from x

(defn- next-up-sequence [^double x ^long n]
  (take n 
        (iterate (fn ^double [^double xi] (Math/nextUp xi))
                 (Math/nextDown x))))

;;----------------------------------------------------------------

(doseq [^ScalarFunctional testf Common/testFns
        ^"[[D" knots Common/allKnots]
  (let [interpolants (map #(interpolate % testf knots)
                          (filter #(valid-knots? % knots)
                                  interpolators))]
    (write-macro-file testf knots interpolants)))

#_(let [^ints files (int-array 1)]
    (doseq [^ScalarFunctional testf (take 1 Common/testFns)
            ^"[[D" knots (take 1 Common/allKnots)]
      (println)
      (println (.toString testf))
      (println (.safeName testf))
      (println (Arrays/toString ^doubles (aget knots 0))
               (Arrays/toString ^doubles (aget knots 1)))
      (println (Common/knotString knots))
      (let [^ints counter (int-array 1)]
        (doseq [^Class interpolator (take 1 interpolators)]
          (when (valid-knots? interpolator knots)
            (let [interpolant (interpolate interpolator testf knots)]
              (when (= interpolator (class interpolant))
                (aset-int counter 0 (inc (aget counter 0)))
                (println (.getSimpleName interpolator))
                (println (.toString interpolant))))))
        (when (< 0 (aget counter 0))
          (println (s/join "." 
                           [(.safeName testf)  
                            (Common/knotString knots)
                            "micro"
                            "tsv"])) 
          (println "interpolants:" (aget counter 0))
          (aset-int files 0 (inc (aget files 0))))))
    (println "files:" (aget files 0)))

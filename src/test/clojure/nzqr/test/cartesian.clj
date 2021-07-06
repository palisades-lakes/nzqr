(set! *warn-on-reflection* true)
;;(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns nzqr.test.cartesian
  
  {:doc "Unit tests for nzqr.cartesian"
   :author "palisades dot lakes at gmail dot com"
   :version "2021-04-23"}
  
  (:refer-clojure :exclude [iterate])
  (:require [clojure.test :as test]
            [nzqr.cartesian :as cartesian]))
;; mvn clojure:test -Dtest=nzqr.test.cartesian
;;----------------------------------------------------------------
;; example p 25: iterate
;; type hints to eliminate boxed math warnings, could just disable
;; Note: only <code>double</code>  and <code>long</code>
;; primitive hints are supported (why?)
;; Note: potential for off-by-1 error between direct (scheme)
;; version and use of <code>clojure.core/iterate</code>

;; translation from scheme

(defn- iterate [^long n f]
  (if (zero? n)
    identity
    (cartesian/compose f (iterate (dec n) f))))

(defn- square [^long x] (* x x))

(defn- revec [v] (vec (reverse v)))
;;----------------------------------------------------------------
(test/deftest test-compose
  
  (test/testing 
    "non-associativity of <code>clojure.lang/comp</code>"
    (let [add1 (fn [x] (+ 1 x))
          mul2 (fn [x] (* 2 x))
          sqrt (fn [x] (Math/sqrt x))
          c21 (comp (comp sqrt mul2) add1)
          c12 (comp sqrt (comp mul2 add1))
          c3 (comp sqrt mul2 add1)]
      (test/is (not (= c21 c12 c3)))))
  
  (test/testing 
    "2 function composition"
    (let [fg (cartesian/compose 
               (fn [x] [:foo x])
               (fn [x] [:bar x]))]
      (test/is (= [:foo [:bar :z]] 
                  (fg :z)))))
  
  (test/testing 
    "iterate composition"
    (let [fg (cartesian/compose 
               (fn [x] [:foo x])
               (fn [x] [:bar x]))]
      (test/is (= 390625 ((iterate 3 square) 5)))))
  
  (test/testing 
    "parallel-split"
    (test/is (= [[:bar [:a :b :c]] [:foo [:a :b :c]]]
                ((cartesian/parallel-split 
                   revec (fn [x] [:foo x]) (fn [x] [:bar x]))
                  [:a :b :c]))))
  
  (test/testing 
    "parallel-diagonal"
    (test/is (= [[:e :d :c] [:b :a]]
                ((cartesian/parallel-diagonal 
                   revec revec revec)
                  [[:a :b] [:c :d :e]]))))
  )

;;----------------------------------------------------------------
;;(run-tests)
;;----------------------------------------------------------------

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
(ns ^{:author "palisades dot lakes at gmail dot com"
      :date "2021-10-25"
      :doc 
      "Generally useful stuff with no obvious better location" }
    
    nzqr.commons.core
  
  (:refer-clojure :exclude [contains? name time])
  (:require [clojure.pprint :as pp]
            [clojure.string :as s])
  (:import [java.lang.reflect Method]
           [java.util Collection HashMap Iterator List Map]
           [java.time LocalDateTime]
           [java.time.format DateTimeFormatter]
           [com.google.common.collect Multimap]
           ;;[zana.java.functions Functions]
           ))
;;----------------------------------------------------------------
(defn jvm-args 
  "Return the command line arguments passed to java. 
   Useful for logging."
  []
  (.getInputArguments 
    (java.lang.management.ManagementFactory/getRuntimeMXBean)))
;;----------------------------------------------------------------
;; try to find a reasonable name for any object
;;----------------------------------------------------------------
;(defn- fn-name [^clojure.lang.IFn f]
;  (Functions/name f)
;  #_(let [strf (str f)]
;      (if (.startsWith ^String strf "clojure.core$constantly$fn")
;        (str "(constantly " (print-str (f nil)) ")")
;        (let [fname (s/replace strf #"^(.+)\$([^@]+)(|@.+)$" "$2")
;              fname (s/replace fname \_ \-)
;              fname (s/replace fname #"--\d+$" "")]
;          fname))))
;;----------------------------------------------------------------
;; Search for a Var whose value is equal to v.
(defn- find-binding [v]
  (ffirst (filter #(= v (var-get (second %))) 
                  (mapcat ns-publics (all-ns)))))
(defn- binding-name [x] 
  (if-let [sym (find-binding x)] (str sym) ""))
;;----------------------------------------------------------------
(defmulti ^String name 
  "Try to find a reasonable name string for <code>x</code>.<br> 
   Try harder than <code>clojure.core/name</code>."
  (fn dispatch-name [x] (class x)))

(defmethod name :default [x] (binding-name x))

(defmethod name nil [x] nil)
(defmethod name String [^String x] x)
(defmethod name Class [^Class x] (.getSimpleName x))
(defmethod name java.io.File [^java.io.File x] (.getName x))

(defmethod name clojure.lang.Namespace [x] (str x))

(defmethod name java.util.Collection [^java.util.Collection x] 
  (binding-name x))

(defmethod name clojure.lang.Fn [^clojure.lang.Fn x] 
  (let [mn (:name (meta x)) 
        bn (binding-name x)
        ;;fn (fn-name x)
        ]
    (cond (not (empty? mn)) mn
          (not (empty? bn)) bn
          ;;(not (empty? fn)) fn
          :else "")))

(defmethod name clojure.lang.IMeta [^clojure.lang.IMeta x] 
  (or (:name (meta x)) (binding-name x)))
(prefer-method name clojure.lang.IMeta java.util.Collection)
(prefer-method name clojure.lang.Fn clojure.lang.IMeta)

(defmethod name clojure.lang.Named [x] (clojure.core/name x))
(prefer-method name clojure.lang.Named java.util.Collection)
(prefer-method name clojure.lang.Named clojure.lang.Fn)
(prefer-method name clojure.lang.Named clojure.lang.IMeta)
;;----------------------------------------------------------------
;; see https://stackoverflow.com/questions/1696693/clojure-how-to-find-out-the-arity-of-function-at-runtime
(defn arity
  
  "Returns the maximum arity of:
    - anonymous functions like `#()` and `(fn [])`.
    - defn'ed functions like `map` or `+`.
    - macros, by passing a var like `#'->`.

  Returns `:variadic` if the function/macro is variadic."
  
  [f]
  
  (let [func (if (var? f) @f f)
        methods (->> func class .getDeclaredMethods
                  (map #(vector (.getName ^Method %)
                                (count (.getParameterTypes ^Method %)))))
        var-args? (some #(-> % first #{"getRequiredArity"})
                        methods)]
    (if var-args?
      :variadic
      (let [max-arity (->> methods
                        (filter (comp #{"invoke"} first))
                        (sort-by second)
                        last
                        second)]
        (if (and (var? f) (-> f meta :macro))
          (- (int max-arity) 2) 
          ;; substract implicit &form and &env arguments
          max-arity)))))
;;----------------------------------------------------------------
;; timing
;;----------------------------------------------------------------
;; like clojure.core.time, prefixes results with a message
(defmacro time
  "Evaluates expr and prints the time it took.  
   Returns the value of expr."
  ([msg expr]
    `(let [start# (System/nanoTime)
           ret# ~expr
           end# (System/nanoTime)
           msec# (/ (Math/round (/ (double (- end# start#)) 
                                   10000.0))
                    100.0)]
       (println ~msg (float msec#) "ms")
       ret#))
  ([expr] `(time (str (quote ~@expr)) ~expr)))
;; like clojure.core.time, but reports results rounded to seconds 
;; and minutes
(defmacro seconds
  "Evaluates expr and prints the time it took.
  Returns the value of expr."
  ([msg & exprs]
    (let [expr `(do ~@exprs)]
      `(let [
             ^DateTimeFormatter fmt#
             (DateTimeFormatter/ofPattern "yyyy-MM-dd HH:mm:ss")]
         (println ~msg (.format fmt# (LocalDateTime/now)))
         (let [start# (System/nanoTime)
               ret# ~expr
               end# (System/nanoTime)
               msec# (/ (double (- end# start#)) 1000000.0)
               sec# (/ msec# 1000.0)
               min# (/ sec# 60.0)]
           (println ~msg (.format fmt# (LocalDateTime/now))
                    (str "(" (int (Math/round msec#)) "ms)"
                         " ("(int (Math/round min#))  "m) "
                         (int (Math/round sec#)) "s"))
           ret#))))
  ([exprs] `(seconds "" ~@exprs)))
;;----------------------------------------------------------------
(defn print-stack-trace
  ([] (.printStackTrace (Throwable.)))
  ([& args]
    (let [^String msg (s/join " " args)]
      (.printStackTrace (Throwable. msg)))))
;;----------------------------------------------------------------
(defn pprint-str
  "Pretty print <code>x</code> without getting carried away..."
  ([x length depth]
    (binding [*print-length* length
              *print-level* depth
              pp/*print-right-margin* 160
              pp/*print-miser-width* 128
              pp/*print-suppress-namespaces* true]
      (with-out-str (pp/pprint x))))
  ([x length] (pprint-str x length 8))
  ([x] (pprint-str x 10 8)))
;;----------------------------------------------------------------
(defmacro echo 
  "Print the expressions followed by their values. 
   Useful for quick logging."
  [& es]
  `(do
     ~@(mapv (fn [e] 
               `(print 
                  "\n"
                  (pprint-str (quote ~e) 60)
                  "->\n" 
                  (pprint-str ~e 60))) 
             es)))
;;----------------------------------------------------------------
(defmacro echo-types 
  "Print the expressions followed by their values and types. 
   Useful for quick logging.

   TODO: detect and format short expressions/values on one line;
   newlines between expression, value, and type for
   shorter ones. Desired line length as optional arg?
   Line up expr, value, type in columns?"
  [& es]
  `(do
     ~@(mapv (fn [e] 
               `(let [v# ~e]
                  (println (quote ~e) "->" v# ":" (type v#)))) 
             es)))
;;----------------------------------------------------------------

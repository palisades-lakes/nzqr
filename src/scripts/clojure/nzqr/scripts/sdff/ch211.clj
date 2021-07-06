;; clj src/scripts/clojure/nzqr/scripts/sdff/ch211.clj
(set! *warn-on-reflection* true)
;;(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns nzqr.scripts.sdff.ch211
  
  {:doc "examples from SDFF ch 2.1.1"
   :author "palisades dot lakes at gmail dot com"
   :version "2021-04-21"}
  
  (:refer-clojure :exclude [iterate])
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [nzqr.commons.core :as scc]
            [nzqr.cartesian :as cartesian])
  (:import 
    [java.lang Math]
    [clojure.lang IFn]))

;;----------------------------------------------------------------
;; check associativity of clojure <code>comp</code>?

  
;;----------------------------------------------------------------
;; simplest translation of example p 24

(defn compose [f g]
  (fn the-composition [x] (f (g x))))

;;----------------------------------------------------------------
;; example p 24
;; clojure.core

;(let [fgc (comp (fn [x] (list :foo x))
;                (fn [x] (list :bar x)))
;      fg0 (compose (fn [x] (list :foo x))
;                   (fn [x] (list :bar x)))
;      fg1 (cartesian/compose
;(fn [x] (list :foo x))
;                    (fn [x] (list :bar x)))]
;  
;  (scc/echo
;    (fgc :z)
;    (fg0 :z)
;    (fg1 :z)))
;;----------------------------------------------------------------
;; example p 25: iterate
;; type hints to eliminate boxed math warnings, could just disable
;; Note: only <code>double</code>  and <code>long</code>
;; primitive hints are supported (why?)
;; Note: potential for off-by-1 error between direct (scheme)
;; version and use of <code>clojure.core/iterate</code>

;; translation from scheme

(defn iterate [^long n f]
  (if (zero? n)
    identity
    (compose f (iterate (dec n) f))))

;; clojure.core/iterate returns an infinite lazy sequence
;; might be worth a performance test, comparing to a more direct
;; implementation

(defn iterate1 [^long n f]
  (fn iterated [x] 
    (last (take (inc n) (clojure.core/iterate f x)))))

;;----------------------------------------------------------------
(defn square [^long x] (* x x))

(scc/echo 
  ((iterate 3 square) 5)
  ((iterate0 3 square) 5))

(scc/echo 
  ((cartesian/parallel-split reverse
                             (fn [x] [:foo x])
                             (fn [x] [:bar x]))
    [:a :b :c])
  )

;;----------------------------------------------------------------
;; Arity p 27, Multiple values p 30
;;
;; This example demonstrates how functions fail to be first class
;; in clojure:
;; (1) No built-in way to get at the arity; instead resort
;; to Java reflection depending on undocumented internal 
;; interfaces.
;; (2) No straightforward way to create a function with arity
;; calculated at runtime.
;; Original Scheme code seems like a hack too, with "metadata"
;; stored in a global, presuambly unsynchronized, hashtable.
;; Also depends on functions objects either being unique,
;; or with expected notion of equality. This isn't true of
;; clojure (at least it wasn't in the past). It was possible
;; load the same function definition multiple times, and
;; resulting instances were not <code>.equal</code>.
;; Adding "metadata" at runetime, either to the function object,
;; or to a separate hashtable, won't necessarily be the same
;; for all instances.
;;
;; As written, the Arity section is an example of what not to do.
;; Bug prone, relying on obscure functionality only in a 
;; particular dialect of Scheme, needlessly restrictive.
;; Conceptually just wrong, functions don't have a particular
;; arity.
;;
;; A better approach acknowledges function domains and codomains
;; (I'll make them explicit later). 
;; The existence of varying arities is in effect just syntactic
;; sugar over the sequences that are the elements of the domains.
;; The functionality here can then be implemented using
;; <code>compose</code> and <code>parallel-compose</code>
;; to combine arbitrary functions and transformations between
;; domains;

;;----------------------------------------------------------------

(scc/echo
  ((cartesian/parallel-diagonal reverse reverse reverse)
    [[:a :b] [:c :d :e]])
  )


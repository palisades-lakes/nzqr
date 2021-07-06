;; clj src/scripts/clojure/nzqr/scripts/sdff/ch23.clj
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns nzqr.scripts.sdff.ch23
  
  {:doc "examples from SDFF ch 2.3"
   :author "palisades dot lakes at gmail dot com"
   :version "2021-05-02"}
  
  #_(:refer-clojure :exclude [iterate])
  (:require [clojure.java.io :as io]
            [clojure.string :as s]
            [nzqr.commons.core :as scc]
            [nzqr.cartesian :as cartesian])
  (:import 
    [java.lang Math]
    [clojure.lang IFn IFn$DD]))

;;----------------------------------------------------------------
;; Could possibly use clojure.math.numeric-tower 
;; for some functions,
;; to maybe get exact rational computation, 
;; but it doesn't have <code>atan</code>,
;; so we would be falling back to something else anyway.
;; Last time I checked, a few years ago, clojure.lang.Ratio
;; had lots of problems, so I'm avoinding that as well.
;; To begin, use <code>java.lang.Math</code>
;; and <code>double</double>.
;;----------------------------------------------------------------


(defn gas-law-volume ^double [^double pressure 
                              ^double temperature 
                              ^double amount]
  (let [gas-constant (double 8.3144621)] ;; J/(K*mol) 
    (/ (* gas-constant amount temperature)
       pressure)))

;;(def pi (* 4 (Math/atan2 1 1))) == Math/PI
;; (def three-over-4pi (/ 3.0 (* 4 pi))) == three-over-4PI

(let [one-third (/ 1.0 3.0)
      three-over-4PI (/ 3 (* 4 Math/PI))]
  (defn sphere-radius ^double [^double volume]
    (Math/pow (* three-over-4PI volume) one-third)) )

;;----------------------------------------------------------------

(scc/echo
  ;  pi
  ;  (- Math/PI pi)
  ;  three-over-4pi
  ;  (- three-over-4PI three-over-4pi)
  (sphere-radius (/ (* 4 Math/PI) 3))
  )

;;----------------------------------------------------------------
;; <code>make-unit-conversion</code> undefined in SDFF,
;; as far as I can tell. 
;;
;; A quick and dirty Clojure closure
;; implementation follows.
;;
;; TODO: Probably deftype implementing clojure.lang.IFn
;; and some Invertible interface would be better.
;; A minimally reasonable version would just be an Invertible
;; function, nothing about "unit converters" required.
;; An Invertible function interface would explicit domain and 
;; codomain, 
;; at least one (pseudo-random) generator for the domain and
;; an equivalence relation for the codomain, to used used in
;; testing whether the 'inverse' really is one.

(defn make-unit-conversion ^IFn [^IFn$DD f ^IFn$DD f-inverse]
  (fn 
    ;; zero arity call returns inverse converter
    (^IFn []  (make-unit-conversion f-inverse f))
    (^double [^double x] (f x))))

(defn invert ^IFn [^IFn converter] (converter))

;;----------------------------------------------------------------
;; Note: this is the affine conversion for absolute temperature.
;; The book does not consider the difference between (affine) 
;; absolute temperature and (linear) change in temperature.
;; Could do these as general 'affine' 
;; <code>double</code> -> <code>double</code>,
;; with inverse generated automatically.
;; Could also implement with Rational or RationalFloat
;; for exact results.
;; TODO: 

(def ^IFn fahrenheit-to-celsius
  (make-unit-conversion 
    (fn ^double [^double f] (/ (* 5 (- f 32)) 9))
    (fn ^double [^double c] (+ (/ (* 9 c) 5) 32))))

(def ^IFn celsius-to-kelvin
  (let [zero-celsius 273.15] ; kelvin degrees
    (make-unit-conversion 
      (fn ^double [^double c] (+ c zero-celsius))
      (fn ^double [^double k] (- k zero-celsius)))))

;;----------------------------------------------------------------

(scc/echo
  (fahrenheit-to-celsius -40)
  (fahrenheit-to-celsius 32)
  ((invert fahrenheit-to-celsius) 20)
  ((comp celsius-to-kelvin fahrenheit-to-celsius) 80)
  )

;;----------------------------------------------------------------
;; converters missing in text
;; TODO: get more digits for ratios?

(def ^IFn pound-to-newton
  (make-unit-conversion 
    (fn ^double [^double pounds] (/ pounds 0.22480894387))
    (fn ^double [^double newtons] (* newtons 0.22480894387))))

(def ^IFn inch-to-meter
  (make-unit-conversion 
    (fn ^double [^double inches] (/ inches 39.3700787))
    (fn ^double [^double meters] (* meters 39.3700787))))

;; Note: in the text, there is no inverse, not a converter!
;; I'm manually inverting the composition, which wouldn't
;; be necessary if functions compositions were first class.

(def ^IFn psi-to-nsm
  (make-unit-conversion 
    (comp pound-to-newton
          (invert inch-to-meter)
          (invert inch-to-meter))
    (comp inch-to-meter 
          inch-to-meter 
          (invert pound-to-newton))))

;;----------------------------------------------------------------

(scc/echo
  ((invert inch-to-meter)
    (sphere-radius
      (gas-law-volume
        (psi-to-nsm 14.7)
        ((comp celsius-to-kelvin fahrenheit-to-celsius) 68)
        1))))

;;----------------------------------------------------------------


;; core src/scripts/clojure/nzqr/scripts/sdff/ch232.core
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns nzqr.scripts.sdff.ch232
  
  {:doc "examples from SDFF ch 2.3.2"
   :author "palisades dot lakes at gmail dot com"
   :version "2021-05-02"}
  
  (:refer-clojure :exclude [*])
  (:require [clojure.core :as core]
            [clojure.java.io :as io]
            [clojure.string :as s]
            [nzqr.commons.core :as scc]
            [nzqr.cartesian :as cartesian])
  (:import 
    [java.lang Math]
    [clojure.lang IFn IFn$DD Keyword]))

;;----------------------------------------------------------------

(defn gas-law-volume ^double [^double pressure 
                              ^double temperature 
                              ^double amount]
  (let [gas-constant (double 8.3144621)] ;; J/(K*mol) 
    (/ (core/* gas-constant amount temperature)
       pressure)))

;;(def pi (* 4 (Math/atan2 1 1))) == Math/PI
;; (def three-over-4pi (/ 3.0 (* 4 pi))) == three-over-4PI

(let [one-third (/ 1.0 3.0)
      three-over-4PI (/ 3 (core/* 4 Math/PI))]
  (defn sphere-radius ^double [^double volume]
    (Math/pow (core/* three-over-4PI volume) one-third)) )

;;----------------------------------------------------------------

(defn make-converter ^IFn [^IFn$DD f ^IFn$DD f-inverse]
  (fn 
    ;; zero arity call returns inverse converter
    (^IFn []  (make-converter f-inverse f))
    (^double [^double x] (f x))))

;;----------------------------------------------------------------
;; group operations
;;----------------------------------------------------------------

(defn invert ^IFn [^IFn u] (u))

;; order of args reverse of comp!
(defn * ^IFn [& us]
  (make-converter (apply comp (reverse us))
                  (apply comp (map invert us))))

(defn divide ^IFn [^IFn u0 ^IFn u1] (* u0 (invert u1)))

;; tail recursive version?
(defn pow ^IFn [^IFn u ^long n]
  (cond (zero? n) u
        (neg? n) (invert (pow u (- n)))
        :else (* u (pow u (dec n)))))

;;----------------------------------------------------------------
;; A two key map in-unit, out-unit -> converter
;; TODO: do we still need the converter to hold its inverse?

(def ^:private converters (atom {}))

(defn register [from 
                to 
                ^IFn$DD converter]
  ;; TODO: should we check for existing converter
  (swap! converters assoc-in [from to] converter)
  (swap! converters assoc-in [to from] (invert converter)))

(defn get-converter ^IFn [^Keyword from
                          ^Keyword to]
  (if (= from to)
    identity
    (let [c (get-in @converters [from to])]
      (assert (not (nil? c)) [from to])
      c)))

;----------------------------------------------------------------

(register 
  :fahrenheit :celsius 
  (make-converter 
    (fn ^double [^double f] (/ (core/* 5 (- f 32)) 9))
    (fn ^double [^double c] (+ (/ (core/* 9 c) 5) 32))))

(register 
  :celsius :kelvin
  (let [zero-celsius 273.15] ; kelvin degrees
    (make-converter 
      (fn ^double [^double c] (+ c zero-celsius))
      (fn ^double [^double k] (- k zero-celsius)))))

;;----------------------------------------------------------------

(register
  :pound :newton
  (make-converter 
    (fn ^double [^double pounds] (/ pounds 0.22480894387))
    (fn ^double [^double newtons] (core/* newtons 0.22480894387))))

(register
  :inch :meter
  (make-converter 
    (fn ^double [^double inches] (/ inches 39.3700787))
    (fn ^double [^double meters] (core/* meters 39.3700787))))

(register
  :fahrenheit :kelvin 
  (* (get-converter :fahrenheit :celsius) 
     (get-converter :celsius :kelvin)))

;;----------------------------------------------------------------
;; Monomial ratios
;;----------------------------------------------------------------
;; SDFF represents the non-base elements of the converter group
;; as quoted expressions. This creates the issue what to do with
;; different, but equivalent expressions. This may be covered 
;; later with an evaluator for expressions returning the reduced
;; form. 
;; Here, I think it's better to stop and think a bit.
;; A converter as defined here, is a monomial ratio:
;; essentially a monomial
;; with both positive and negative powers.
;; (Is the a standard name for this?)
;;
;; In any case, I think it's safer to use a representations
;; of the monomimal ratios as the key into the converter table.
;; An easy idomatic Clojure approach to this is to use a hashmap
;; with unit keywords as keys/variables and 
;; long values as the exponents. 
;;
;; Modest improvements to get-converter could eliminate the need
;; to define and register the monomial ratio cases.
;; Would be easy enough to generate them, assuming the base
;; converters have been registered. 
;; However, I'm going to skip that for now.
;; The effort would be better spent on an implementation
;; that fixes the conceptual problems, such as reifying
;; measurable quantity group (length, time, etc.)
;; and organizing the converters around that.
;; If nothing else, I'd want to replace the one-off parts
;; of this with general parts (eg invertible functions
;; with access to (co)domain sets, algebraic structures, etc)

(register 
  {:pound 1 :inch -2}
  {:newton 1 :meter -2}
  (make-converter 
    (comp (get-converter :pound :newton)
          (get-converter :meter :inch)
          (get-converter :meter :inch))
    (comp (get-converter :inch :meter) 
          (get-converter :inch :meter) 
          (get-converter :newton :pound))))

;;----------------------------------------------------------------

(scc/echo @converters)

;;----------------------------------------------------------------

(defn specialize ^IFn [^IFn procedure
                       implicit-output-unit
                       & implicit-input-units]
  (fn specializer ^IFn [specific-output-unit
                        & specific-input-units]
    (let [output-converter (get-converter implicit-output-unit
                                          specific-output-unit)
          input-converters (mapv get-converter 
                                 specific-input-units
                                 implicit-input-units)]
      (fn specialized-procedure ^IFn [& arguments]
        (assert (= (count input-converters) (count arguments)))
        (output-converter
          (apply 
            procedure
            (map (fn [converter argument] (converter argument))
                 input-converters
                 arguments)))))))

;;----------------------------------------------------------------

(def glsr-SI (comp sphere-radius gas-law-volume))

(def glsr-specializer (specialize glsr-SI
                                   :meter
                                   {:newton 1 :meter -2}
                                   :kelvin
                                   :mole))

(def glsr-US (glsr-specializer :inch 
                               {:pound 1 :inch -2}
                               :fahrenheit
                               :mole))

(scc/echo
  (glsr-US 14.7 68 1))

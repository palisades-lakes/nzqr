;; core src/scripts/clojure/nzqr/scripts/sdff/ch3.clj
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns nzqr.scripts.sdff.ch3
  
  {:doc "SDFF chapter 3"
   :author "palisades dot lakes at gmail dot com"
   :version "2021-05-11"}
  
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

(scc/echo
  (glsr-US 14.7 68 1))

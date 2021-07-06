;; clj src/scripts/clojure/nzqr/scripts/reduce.clj
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns nzqr.scripts.reduce
  
  {:doc "fix reduce latex output"
   :author "palisades dot lakes at gmail dot com"
   :version "2018-10-29"}
  
  (:require [clojure.java.io :as io]
            [clojure.string :as s]))
;;----------------------------------------------------------------

(defn- frac [^String line]
  #_(println "in:" line)
  (let [result
        (s/replace 
          line
          #"^( .+) & = [\s]*\\left\( (.+) \\right\)[\s]* / [\s]*\\left\( (.+?) \\right\)[\s]*(\\?\\?)[\s]*$"
          "$1 & = \\\\frac\n{$2}\n{$3} $4")]
    #_(println "out:" result)
    result))

;;----------------------------------------------------------------

(defn- repair [^String fpath]
  (println fpath)
  (let [f (io/file fpath)
        parent (.getParent f)
        fname (s/join "." (butlast (s/split (.getName f) #"\.")))
        text (slurp f)
        text (s/replace text #"\$$" "")
        text (s/replace text "/" " / ")
        text (s/replace text "\\left(" " \\left( ")
        text (s/replace text "\\right)" " \\right) ")
        text (s/replace text "{equation}" "{align}")
        text (s/replace text "\\left\\{" "")
        text (s/replace text "\\right\\}" "")
        text (s/replace text "\r" "")
        text (s/replace text "\n" " ")
        text (s/replace text "\\begin{align}" "\n\\begin{align}\n")
        text (s/replace text "\\end{align}" "\n\\end{align}\n\n")
        text (s/replace text "," " \\\\\n  ")
        text (s/replace text "=" " & = ")
        text (s/replace text "-" " - ")
        text (s/replace text #"([axyd]{1})([0123]{1})" "$1_$2")
        text (s/replace text "sqrt" "\\sqrt")
        text (s/replace text #"mu([0123]{1})" "\\\\mu_$1")
        #_(println text)
        text (s/join 
               "\n"
               (mapv 
                 frac 
                 (filter 
                   #(not 
                      (or (s/includes? % "***")
                          (s/includes? % "\\documentstyle")
                          (s/includes? % "\\begin{doc")
                          (s/includes? % "\\end{doc")
                          (s/includes? % "end$")
                          ))
                   (s/split-lines text))))
        #_(println text)
        ]
    (spit (io/file parent (str fname ".tex")) text)))

;;----------------------------------------------------------------

(defn- filetype [f]
  (last (s/split (.getName (io/file f)) #"\.")))
        
(defn rawtex? [f] (= "rawtex" (filetype f)))

;;----------------------------------------------------------------

#_(repair "docs/interpolation/monomial-yddd4.rawtex")

(doseq [path (filter rawtex? 
                     (file-seq (io/file "docs/interpolation")))]
  (repair path))


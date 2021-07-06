;; clj src/scripts/clojure/nzqr/scripts/plots.clj
(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns nzqr.scripts.expresso
  
  {:doc "solve symbolic systems of equations for interpolating 
         polynomials"
   :author "palisades dot lakes at gmail dot com"
   :version "2018-10-18"}
  
  (:require [clojure.pprint :as pp]
            #_[numeric.expresso.core :as nec]))
;;----------------------------------------------------------------
;; Affine monomial yy

;(def affine-monomial 
;  (first
;    (nec/solve 
;      '[a0 a1]
;      (nec/ex (= y0 (+ a0 (* a1 x0))))
;      (nec/ex (= y1 (+ a0 (* a1 x1)))))))
;
;#{{a1 (+ (* y1 
;            (+ (/ x1) 
;               (* (** (/ x1) 2) 
;                  -1 
;                  x0 
;                  (/ (+ (/ x1) (- x0) -1)))))
;         (* y0 
;            (/ (+ (/ x1) (- x0) -1))
;            (/ x1))), 
;   a0 (* (/ (+ (/ x1) (- x0) -1))
;         (+ (* y1 (/ x1) x0)
;            (- y0)))}}
;(pp/pprint
;  (map 
;    (fn [[v expr]]  [v (nec/simplify expr)])
;   affine-monomial ))
;
;; a1
;(+ (* (/ x1) 
;      (+ y1 
;         (* y0 
;            (/ (+ (/ x1) (- x0) -1)))))
;   (* (/ (** x1 2)) 
;      -1 x0 
;      (/ (+ (/ x1) (- x0) -1)) 
;      y1))
;(pp/pprint
;  (map 
;    (fn [[v expr]]  [v (nec/simplify expr)])
;    (first
;      (nec/solve 
;        '[a0 a1]
;        (nec/ex (= y0 (+ a0 (* a1 x0))))
;        (nec/ex (= y1-y0 (* a1 x1-x0)))))))
; !!! WRONG ANSWER: !!!
;[a1 (* y1-y0 (/ x1-x0))] 
;[a0 (+ y0 (* -1 y1-y0 x1-x0 x0))]
;
;(pp/pprint
;  (map 
;    (fn [[v expr]]  [v (nec/simplify expr)])
;    (first
;      (nec/solve 
;        '[a0 a1]
;        (nec/ex (= y0 (+ a0 (* a1 x0))))
;        (nec/ex (= y1-y0 (* a1 x1-x0)))))))
;
;(pp/pprint
;  (map 
;    (fn [[v expr]]  [v (nec/simplify expr)])
;    (first
;      (nec/solve 
;        '[a0 a1]
;        (nec/ex (= y0 (+ a0 (* a1 x0))))
;        (nec/ex (= (+ y1 (- y0)) (* a1 (- x1 x0))))))))
;
;(def e0 (nec/ex (- y0 a0 (* a1 x0))))
;(def e1 (nec/ex (- y1 a0 (* a1 x1))))
;(def e0 (nec/ex (- y0 (+ a0 (* a1 x0)))))
;(def e1 (nec/ex (- y1 (+ a0 (* a1 x1)))))
;(def e1-e0 (nec/ex (= 0 (- ~e0 ~e1))))
;(def e1+e0 (nec/ex (= 0 (+ ~e0 ~e1))))
;
;(try
;  (nec/solve 
;    '[a0 a1]
;    e1-e0
;    e1+e0
;    )
;  (catch Throwable t
;    (.printStackTrace t)
;    (throw t)))
;
;(pp/pprint
;  (nec/simplify
;    (nec/solve 
;      '[a0 a1]
;      (nec/ex (= 0 (+ ~e0 ~e1)))
;      (nec/ex (= 0 (- ~e0 ~e1))))))
;
;(nec/expression? '(= (- y1 y0) (* a (- x1 x0))))
;(nec/simplify 
;  (nec/solve '[a] '(= (- y1 y0) (* a (- x1 x0)))))


;;----------------------------------------------------------------
;; Affine monomial yd

;(pp/pprint
;  (nec/simplify
;    (nec/solve 
;      '[a0 a1]
;      (nec/ex (= y0 (+ a0 (* a1 x0))))
;      (nec/ex (= d1 a1)))))
;
;(nec/ex
;  ~(nec/differentiate 
;     '[x1]
;     (nec/ex (+ a0 (* a1 x1)))))
;
;(pp/pprint
;  (nec/simplify
;    (nec/solve 
;      '[a0 a1]
;      (nec/ex (= y0 (+ a0 (* a1 x0))))
;      (nec/ex (= d1 
;                 ~(nec/differentiate 
;                    '[x1]
;                    (nec/ex (+ a0 (* a1 x1)))))))))

;;----------------------------------------------------------------

#_(nec/solve 'blue
             (nec/ex (= pencils (+ green white blue red)))
             (nec/ex (= (/ pencils 10) green))
             (nec/ex (= (/ pencils 2) white))
             (nec/ex (= (/ pencils 4) blue))
             (nec/ex (= red 45))) ;=> #{{blue 75N}}
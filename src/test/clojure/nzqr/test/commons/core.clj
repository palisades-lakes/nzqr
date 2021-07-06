(set! *warn-on-reflection* true)
;;(set! *unchecked-math* :warn-on-boxed)
;;----------------------------------------------------------------
(ns nzqr.test.commons.core
  
  {:doc "Unit tests for nzqr.commons.core"
   :author "palisades dot lakes at gmail dot com"
   :version "2021-04-20"}
  
  (:require [clojure.test :as test]
            [nzqr.commons.core :as scc]))
;; mvn clojure:test -Dtest=nzqr.test.commons.core
;;----------------------------------------------------------------
(defmacro ^:private m ([a]) ([a b]))
(defmacro ^:private mx [])

(test/deftest test-arity
  (test/testing "with an anonymous #(… %1) function"
    (test/is (= 1           (scc/arity #(+ % 32))))
    (test/is (= 1           (scc/arity #(+ %1 32))))
    (test/is (= 2           (scc/arity #(+ %1 %2))))
    (test/is (= 13          (scc/arity #(+ %1 %2 %3 %4 %5 %6 %7 %8 %9 %10 %11 %12 %13))))
    (test/is (= :variadic   (scc/arity #(apply + %&))))
    (test/is (= :variadic   (scc/arity #(apply + % %&)))))
  (test/testing "with an anonymous (fn [] …) function"
    (test/testing "single body"
      (test/is (= 0         (scc/arity (fn []))))
      (test/is (= 1         (scc/arity (fn [a]))))
      (test/is (= 2         (scc/arity (fn [a b]))))
      (test/is (= 20        (scc/arity (fn [a b c d e f g h i j k l m n o p q r s t]))))
      (test/is (= :variadic (scc/arity (fn [a b & more])))))
    (test/testing "multiple bodies"
      (test/is (= 0         (scc/arity (fn ([])))))
      (test/is (= 1         (scc/arity (fn ([a])))))
      (test/is (= 2         (scc/arity (fn ([a]) ([a b])))))
      (test/is (= :variadic (scc/arity (fn ([a]) ([a b & c])))))))
  (test/testing "with a defined function"
    (test/is (= :variadic   (scc/arity map)))
    (test/is (= :variadic   (scc/arity +)))
    (test/is (= 1           (scc/arity inc))))
  (test/testing "with a var to a macro"
    (test/is (= :variadic   (scc/arity #'->)))
    (test/is (= 2           (scc/arity #'m)))
    (test/is (= 0           (scc/arity #'mx)))))

;;(run-tests)
;;----------------------------------------------------------------

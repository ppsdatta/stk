(ns stk.core-test
  (:require [clojure.test :refer :all]
            [stk.core :refer :all]))

(deftest stk-eval-test-basic
  (testing "Testing basic operations"
    (is (empty? (stk-eval [{:double [dup plus]} 1 2 2 plus :double :double mult dup minus .]
                          '()
                          {}))))
  (testing "Test division"
    (is (= (first (stk-eval [4 2 div] '() {})) 2)))

  (testing "Test exponentiation"
    (is (= (first (stk-eval [2 3 pow] '() {})) 8.0)))

  (testing "Test swap"
    (is (= (apply vector (stk-eval [2 3 swap] '() {})) [2 3])))

  (testing "Test rotate"
    (is (= (apply vector (stk-eval [2 3 4 rot] '() {})) [3 2 4])))

  (testing "Test variable"
    (is (= (apply
             vector
             (stk-eval [{:square [dup mult] :a [43] :b [3]}
                        :a :b minus :square] '() {}))
           [1600]))))

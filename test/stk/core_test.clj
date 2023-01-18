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

(deftest stk-eval-test-rank-polymorphism
  (testing "Testing addition"
    (is (= (first
             (stk-eval [2 [3 4 5] plus]
                       '()
                       {}))

           [5 6 7]))
    (is (= (first
             (stk-eval [[1 2 3] 8 plus]
                       '()
                       {}))

           [9 10 11]))
    (is (= (first
             (stk-eval [[1 2 3] [3 2 1] plus]
                       '()
                       {}))

           [4 4 4]))
    (is (thrown? IllegalArgumentException
                 (stk-eval [[1 2 3] [2] plus]
                           '()
                           {})))
    (is (thrown? IllegalArgumentException
                 (stk-eval [[1 2 3] [2 3 4 5 6] plus]
                           '()
                           {}))))

  (testing "Testing minus"
    (is (= (first
             (stk-eval [2 [3 4 5] minus]
                       '()
                       {}))

           [-1 -2 -3]))
    (is (= (first
             (stk-eval [[1 2 3] 8 minus]
                       '()
                       {}))

           [-7 -6 -5]))
    (is (= (first
             (stk-eval [[1 2 3] [3 2 1] minus]
                       '()
                       {}))

           [-2 0 2]))
    (is (thrown? IllegalArgumentException
                 (stk-eval [[1 2 3] [2] minus]
                           '()
                           {})))
    (is (thrown? IllegalArgumentException
                 (stk-eval [[1 2 3] [2 3 4 5 6] minus]
                           '()
                           {}))))

  (testing "Testing multiply"
    (is (= (first
             (stk-eval [2 [3 4 5] mult]
                       '()
                       {}))

           [6 8 10]))
    (is (= (first
             (stk-eval [[1 2 3] 8 mult]
                       '()
                       {}))

           [8 16 24]))
    (is (= (first
             (stk-eval [[1 2 3] [3 2 1] mult]
                       '()
                       {}))

           [3 4 3]))
    (is (thrown? IllegalArgumentException
                 (stk-eval [[1 2 3] [2] mult]
                           '()
                           {})))
    (is (thrown? IllegalArgumentException
                 (stk-eval [[1 2 3] [2 3 4 5 6] mult]
                           '()
                           {}))))

  (testing "Testing division"
    (is (= (first
             (stk-eval [12 [3 4 6] div]
                       '()
                       {}))

           [4 3 2]))
    (is (= (first
             (stk-eval [[10 20 30] 5 div]
                       '()
                       {}))

           [2 4 6]))
    (is (= (first
             (stk-eval [[5 5 5] [2 2 2] div]
                       '()
                       {}))

           [5/2 5/2 5/2]))
    (is (thrown? IllegalArgumentException
                 (stk-eval [[1 2 3] [2] div]
                           '()
                           {})))
    (is (thrown? IllegalArgumentException
                 (stk-eval [[1 2 3] [2 3 4 5 6] div]
                           '()
                           {}))))

  (testing "Testing power"
    (is (= (first
             (stk-eval [2 [3 4 5] pow]
                       '()
                       {}))

           [8.0 16.0 32.0]))
    (is (= (first
             (stk-eval [[10 20 30] 2 pow]
                       '()
                       {}))

           [100.0 400.0 900.0]))
    (is (= (first
             (stk-eval [[5 5 5] [2 2 2] pow]
                       '()
                       {}))

           [25.0 25.0 25.0]))
    (is (thrown? IllegalArgumentException
                 (stk-eval [[1 2 3] [2] pow]
                           '()
                           {})))
    (is (thrown? IllegalArgumentException
                 (stk-eval [[1 2 3] [2 3 4 5 6] pow]
                           '()
                           {}))))
  (testing "Testing rank 0 with rank N operation (broadcasting)"
    (is (= (first
             (stk-eval [2 [[3 4 5] [4 5 6]] plus 3 mult]
                       '()
                       {}))
           [[15 18 21] [18 21 24]]))
    (is (= (first
             (stk-eval [[[3 4 5] [4 5 6]] 3 minus]
                       '()
                       {}))
           [[0 1 2] [1 2 3]]))
    (is (= (first
             (stk-eval [10 iota 2 modulo]
                       '()
                       {}))
           [0 1 0 1 0 1 0 1 0 1]))))
(ns serpent-talk.talk-test
  (:require [clojure.test :refer :all]
            [serpent-talk.talk :refer :all]))

(deftest small-test
  (testing "Should return list with small texts"
    (let [testlist ( serpent-talk.talk/makesmallly "this is a test sentence" )]
     ( is (.contains testlist "this-is-a-test-sentence") )
     ( is (.contains testlist "thisIsATestSentence") )
     ( is (.contains testlist "this_is_a_test_sentence") ))))

(deftest big-test
  (testing "Should return list with Big Texts"
    (let [testlist ( serpent-talk.talk/makebigly "this is a test sentence" )]
      ( is (.contains testlist "This_is_a_test_sentence")
      ( is (.contains testlist "ThisIsATestSentence"))))))


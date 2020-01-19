(ns sandbox.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn myfun []
  (str "this is what's up" "yo"))

(println (myfun))

(def counter (atom 0))
(defn inc-counter []
  (swap! counter inc))

(inc-counter)

(println "What a cool thing")

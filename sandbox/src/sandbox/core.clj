(ns sandbox.core
  (:require [sandbox.simpledata :as simpledata])
  (:require [sandbox.infdata :as infdata])
  (:require [sandbox.polyjava :as polyjava])
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

(println (str "The counter is at " @counter))

(simpledata/write-it-out "a sentence")


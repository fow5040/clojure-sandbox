(ns sand
  (:require [clojure.set :as sets])
  (:require [clojure.pprint :as pprint]) 
  (:gen-class))


(defn write-it-out [x]
  "Prints a sentence twice"
  (println x)
  (println x))

;;Let's do some lists
(def alist '(1 4 3 6 9 8 3 4 2 3 "odd one out" 3 4 5))
(def simpvec [9 8 7 6 5 4 3 2 1])

(def oddvec
  (conj simpvec "weird" 3 4 [5 6 7]))

(def cleanlist
  (filter number? alist))

(def vecsAndLists
  {:good1 cleanlist :good2 simpvec :bad1 alist :bad2 oddvec})

(defn list-mykeys [mymap]
  "uses inline func to print a bunch of keys"
  (map #(println %) mymap))

;;define some sets
(def setA #{:thingA :thingB :thingC :thingD})
(def setB #{:thingB :thingC :thingD :thingE :thingF})
(def setC #{:thingC :thingD :thingF})

;;pretty print some set outputs
(pprint/pprint [
  :union (sets/union setA setB)
  :intersection (sets/intersection setA setB)
  :subset? (sets/subset? setC setB)
  :superset? (sets/superset? setC setB)
  ])

;;simple atom
(def p
  (atom 0))
(swap! p inc)

;;show a race condition
;;show that multiple threads can swap
;;show that swap will retry and keep running the same func
(def race
 (atom 0))

(defn inc-print [val]
  (println (str "num" val))
  (inc val))

( defn start-race []
  (let [n 1]
    (future ( swap! race inc-print))
    (future ( swap! race inc-print))
    (future ( swap! race inc-print))
    (future ( swap! race inc-print))
    (future ( swap! race inc-print))
    (future ( swap! race inc-print))
  )
 )

(println @race)
(start-race)





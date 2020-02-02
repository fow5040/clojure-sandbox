(ns sandbox.simpledata
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

;;(dotimes [race 500] (start-race))

(println @race)

;;atoms are handled 1 by 1
;;can use refs instead of atoms
;;synchronous transactions
;; ensure both transactions occur before moving on

(def ref1 (ref 5))
(def ref2 (ref 10))


(let [_ nil]
  ( dosync
   (alter ref1 inc)
   (alter ref2 inc))
  (pprint/pprint (str @ref1 " and " @ref2))
  )


;; use set-ref to explicitly define the value


(def ref3 (ref 10))
(def ref4 (ref 0))

(defn dotransact []
  ( dosync
   (alter ref3 inc)
   (ref-set ref4 (* @ref3 2))
   (pprint/pprint (str @ref3 " and " @ref4))))

(let [t 5]
  (future (dotimes [__ t] (dotransact)))
  (future (dotimes [__ t] (dotransact)))
  (future (dotimes [__ t] (dotransact)))
  )


;; can use agents for asynchronous state updates - i.e. you don't really care about definitively matching state

(def time-bomb (agent 10))

(defn bomb-tick [time]
  (cond
    (> time 1) (- time 1)
    :else (throw (Exception. "*****DETONATED*****"))))

(defn reverse-bomb-tick [time] (inc time))

;; can use send-off instead of send for I/O blocking operations
;; send uses a fixed thread pool, so is good for CPU bound operations
;; send-off uses an expandable thread pool, which is good for I/O bound operations
;; I.E. if you're writing to the log file, the agent may be blocked indefinitely, so use send-off
(dotimes [_ 10]
    (send time-bomb bomb-tick)
 )
;;executing this will fail since the agent is excepted
;;Need to restartagent before executing this
(restart-agent time-bomb 10)
(send time-bomb reverse-bomb-tick )

;;we can also change how agents handle failure
;;  :fail or :continue
(def time-bomb (agent 10))
(set-error-mode! time-bomb :continue)

(defn some-error-handler-fn [myAgent ex]
  (println (str "Val is " @myAgent " and exception was: \n" ex))
  (println "\nResetting agent!")
  ;;janky way to set the value back to 10
  (send time-bomb #(+ (- % %) 10))
  nil)

(set-error-handler! time-bomb some-error-handler-fn)

(dotimes [_ 10]
    (send time-bomb bomb-tick)
 )

(println @time-bomb )

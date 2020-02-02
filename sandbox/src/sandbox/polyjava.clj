(ns sandbox.polyjava
  (:import (java.net InetAddress))
  (:gen-class))

;;let's do some interop
(def astr "tHiS iS a strING")

(.replace astr "i" "eye")

(def netThing
  (InetAddress/getByName "www.google.com"))


;;polymorphism
(defmulti multiprintA
  "Maybe print out numbers or a string?"
  class)

(defmethod multiprintA java.lang.String [input]
  (println (.toUpperCase input)))

(defmethod multiprintA java.net.InetAddress [input]
  (println (.getHostAddress input)))

(defmethod multiprintA java.lang.Long [input]
  (println (str input input input)))

(multiprintA netThing)
(multiprintA astr)
(multiprintA 23142334)

;;can defmulti with arbitrary function
;;why tf this won't work ??
(defmulti multiprintB (fn [value]
                        (if (< value 3)
                          :caseA
                          :caseB)))
                        
(defmethod multiprintB :caseA [_]
  "was case A")

(defmethod multiprintB :caseB [_]
  "was case B")

;;this call always errors out for some reason - it think I'm calling the number
;;(multiprintB 24) 

;;this example works, for whatever reason
(defmulti multiprintC (fn [value]
                        (cond
                          (< value 3) :caseA
                          (> value 10) :caseB
                          :default :caseC)))

(defmethod multiprintC :caseA [_]
  "num is less than 3")

(defmethod multiprintC :caseB [_]
  "num is more than 10")

(defmethod multiprintC :caseC [_]
  "num is probably between 3 and 10")

(defmethod multiprintC :default [_]
  "did you give me a number?")

(multiprintC 14)

;; Let's try again with shorthand notation
(defmulti multiprintD #(cond
                          (< % 3) :caseA
                          (> % 10) :caseB
                          :default :caseC))

(defmethod multiprintD :caseA [_]
  ( println "num is less than 3"))

(defmethod multiprintD :caseB [_]
  ( println "num is more than 10"))

(defmethod multiprintD :caseC [_]
  ( println "num is probably between 3 and 10"))

(map multiprintD [1 2 3 5 6 8 10 12 13] )


;;protocol multiprinta
;;define a func, then extend with protocols

;;(defmulti multiprintE #(if (< % 3) :A :B))
;;(defmethod multiprintE :A [_] (println "isSmall"))
;;(defmethod multiprintE :B [_] (println "isBig"))

(defprotocol protoprint
  (multiprintE [this]))

(extend-protocol protoprint
  java.lang.String
  (multiprintE [this]
    (println "uhhhh this is a string"))

  clojure.lang.Keyword
  (multiprintE [this]
    (case this
      :A (println "isSmall")
      :B (println "isBig")
      (println (str "Unknown Keyword of " this ))))

  java.lang.Long
  (multiprintE [this]
    (if (< this 3)
      (println "isSmall")
      (println "isBig"))))

(map multiprintE ["asdf" :A :B :athing :athing2 1 3 7])

;;How to define custom data types??
;;use records
(defrecord myclass [name amount] )

(def exampleobject (myclass. "a class name" "17 things"))

(class exampleobject)
;;-> sandbox.polyjava.myclass

(.-amount exampleobject)
;; > "17 things"


;;give this object the above defined protoprint protocol
;; gives it the multiprintE method

(defprotocol canbenew
  (isthisnew? [this]))

(defrecord newclass [name]
  canbenew
  (isthisnew? [this]
    "yes"))

(defrecord oldclass [name]
  canbenew
  (isthisnew? [this]
    "no"))

(def oldObj (oldclass. "oldthing"))
(def newObj (newclass. "newthing"))

(isthisnew? oldObj)
(isthisnew? newObj)

;;trippy
;;real world: can write the same data to different data sources
;;  each data source uses a different record type
;; can also use deftype if we don't care about structured data in the record



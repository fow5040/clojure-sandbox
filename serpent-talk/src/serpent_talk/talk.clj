(ns serpent-talk.talk
  (:require [camel-snake-kebab.core :as csk]))


(defn makebigly [inStr]
  (conj [] 
    (csk/->CamelCase inStr)
    (csk/->Snake_case inStr)))

(defn makesmallly [inStr]
  (conj [] 
    (csk/->camelCase inStr)
    (csk/->snake_case inStr)
    (csk/->kebab-case inStr)))

(defn -main [& args]
  (let [input (first args)]
  (println ( str "Bigly output: " ( makebigly input ) ) )
  (println ( str "Smallly output: " ( makesmallly input ) ) )))



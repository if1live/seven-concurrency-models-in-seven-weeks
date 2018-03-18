(ns channels.core
  (:require [clojure.core.async :as async :refer :all
             :exclude [map into reduce merge partition partition-by take]]))

(defn -main [& args]
  ; (def ch1 (chan))
  ; (def ch2 (chan))
  ; (go-loop []
  ;   (alt!
  ;     ch1 ([x] (println "Read" x "from channel 1"))
  ;     ch2 ([x] (println "twice" x "is" (* x 2))))
  ;   (recur))
  ; (>!! ch1 "foo")
  ; (>!! ch2 4)

  ; (def ch (chan))
  ; (let [t (timeout 50)]
  ;   (go (alt!
  ;     ch ([x] (println "read" x "from channel"))
  ;     t (println "time out"))))

  (Thread/sleep 100)
  (println "main"))

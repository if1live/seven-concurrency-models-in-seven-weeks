(ns channels.core
  (:require [clojure.core.async :as async :refer :all
             :exclude [map into reduce merge partition partition-by take]]))

(defn readall!! [ch]
  (loop [coll []]
    (if-let [x (<!! ch)]
      (recur (conj coll x))
      coll)))

(defn writeall!! [ch coll]
  (doseq [x coll]
    (>!! ch x))
  (close! ch))

(defn go-add [x y]
  (<!! (nth (iterate #(go (inc (<! %))) (go x)) y)))

;; Can only recur from tail position
;; ?????
;; (defn map-chan [f from]
;;   (let [to (chan)]
;;     (go-loop []
;;       (when-let [x (<! from)]
;;         (>! to (f x))
;;         (recur))
;;       (close! to))
;;     to))


(defn -main [& args]
  ;; (def ch (chan 10))
  ;; (writeall!! ch (range 0 10))
  ;; (println (readall!! ch))

  ;; (def ch (chan 10))
  ;; (onto-chan ch (range 10))
  ;; (println (<!! (async/into [] ch)))

  ;; (def dc (chan (dropping-buffer 5)))
  ;; (onto-chan dc (range 0 10))
  ;; (Thread/sleep 100)
  ;; (println (<!! (async/into [] dc)))

  ;; (def sc (chan (sliding-buffer 5)))
  ;; (onto-chan sc (range 0 10))
  ;; (Thread/sleep 100)
  ;; (println (<!! (async/into [] sc)))

  ;; (println (time (go-add 10 10)))
  ;; (println (time (go-add 10 1000)))
  ;; (println (time (go-add 10 100000)))

  ;; (def ch (chan 10))
  ;; (def mapped (map-chan (partial * 2) ch))
  ;; (onto-chan ch (range 0 10))
  ;; (println (<!! (async/into [] mapped)))

  ;; (def ch (to-chan (range 0 10)))
  ;; (println (<!! (async/into [] (map< (partial * 2) (filter< even? ch)))))

  (println "main"))

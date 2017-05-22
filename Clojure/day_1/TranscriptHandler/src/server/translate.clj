(ns server.translate
  (:require [clj-http.client :as client]))

(def translator "http://localhost:3001/translate")

(defn translate [text]
  (future
((client/post translator {:body text}) :body)))

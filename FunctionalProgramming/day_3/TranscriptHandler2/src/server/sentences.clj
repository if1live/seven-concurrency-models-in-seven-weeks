(ns server.sentences
  (:require [clojure.string :refer [trim]]))

;; (sentence-split "This is a sentence. Is this?! A fragment")
;; server.core=> ("This is a sentence." "Is this?!" "A fragment")
(defn sentence-split [text]
  (map trim (re-seq #"[^\.!\?:;]+[\.!\?:;]*" text)))

;; server.core=> (is-sentence? "This is a sentence.")
;; "This is a sentence."
;; server.core=> (is-sentence? "A sentence doesn't end with a comma,")
;; nil
(defn is-sentence? [text]
  (re-matches #"^.*[\.!\?:;]$" text))

;; server.core=> (sentence-join "A complete sentence." "Start of another")
;; "Start of another"
;; server.core=> (sentence-join "This is" "a sentence.")
;; "This is a sentence."
(defn sentence-join [x y]
  (if (is-sentence? x) y (str x " " y)))


;; server.core=> (def fragments ["A" "sentence." "And another." "Last" "sentence."])
;; #'server.core/fragments
;; server.core=> (reductions sentence-join fragments)
;; ("A" "A sentence." "And another." "Last" "Last sentence.")
;; server.core=> (filter is-sentence? (reductions sentence-join fragments))
;; ("A sentence." "And another." "Last sentence.")
(defn strings->sentences [strings]
  (filter is-sentence?
          (reductions sentence-join
                      (mapcat sentence-split strings))))

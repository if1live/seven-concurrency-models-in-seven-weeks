(ns philosophers.util)

(defn swap-when!
  "todo doc-string"
  [a pred f & args]
  (loop []
    (let [old @a]
      (if (pred old)
        (let [new (apply f old args)]
          (if (compare-and-set! a old new)
            new
            (recur)))
        nil))))

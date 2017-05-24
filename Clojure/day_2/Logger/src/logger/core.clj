(ns logger.core)

(defn now []
  (System/currentTimeMillis))

(def log-entries (agent []))

(defn log [entry]
  (send log-entries conj [(now) entry]))

(defn -main [& args]
  (log "Something happened")
  (Thread/sleep 100)
  (log "Something else happened")
  (Thread/sleep 100)
  (println @log-entries))

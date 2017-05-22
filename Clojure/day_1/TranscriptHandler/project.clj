(defproject server "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-jetty-adapter "1.5.1"]
                 [compojure "1.5.2"]
                 [schejulure "0.1.4"]
                 [clj-http "3.5.0"]
                 [clj-time "0.7.0"]
                 ;; for remove-vals
                 [org.flatland/useful "0.9.5"]]
  :main server.core)

(defproject channels "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  ;; 0.4, 0.3, 0.2 core/async를 사용할 경우
  ;; 책의 코드 map-chan 선언시
  ;; Can only recur from tail position 가 발생한다
  ;; 그래서 core.async를 구버전으로 맞춤
  ;; https://github.com/islomar/seven-concurrency-models-in-seven-weeks/blob/master/CSP/channels/project.clj
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]]
  :main channels.core)

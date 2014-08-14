(defproject indent-clj "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev {:dependencies [[midje "1.6.0"]]
                   :plugins [[lein-midje "3.0.0"]]}
  :plugins [[lein-cljsbuild "1.0.2"]
            [lein-midje "3.0.0"]]}
  :eval-in-leiningen true)

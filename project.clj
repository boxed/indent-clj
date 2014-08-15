(defproject indent-clj "1.0.1"
  :description "Inferred parenthesis for clojure"
  :url "https://github.com/boxed/indent-clj"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :scm {:name "git"
        :url "https://github.com/boxed/indent-clj"}
  :profiles {:dev {:dependencies [[midje "1.6.0"]]
                   :plugins [[lein-midje "3.0.0"]]}}
  :eval-in-leiningen true)

(ns indent-clj.test-indent
  [:use midje.sweet]
  [:require [leiningen.indent-clj :refer :all]])


(fact
 (foo "foo\n    bar") => "(foo\n    bar)"
 (foo "foo\n    bar\n        baz") => "(foo\n    (bar\n        baz))"
 )

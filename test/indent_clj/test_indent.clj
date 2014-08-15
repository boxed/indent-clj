(ns indent-clj.test-indent
  [:use midje.sweet]
  [:require [leiningen.indent-clj :refer :all]])

(spit "test-output.clj" (foo (slurp "test.indent-clj")))

(fact
; (foo "foo\n  bar") => "(foo\n  bar)"
; (foo "foo\n  bar\n    baz") => "(foo\n  (bar\n    baz))"
 (foo (slurp "test.indent-clj")) => (slurp "test.clj")
 )

(ns reactive-clojure.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [reactive-clojure.core-test]))

(doo-tests 'reactive-clojure.core-test)

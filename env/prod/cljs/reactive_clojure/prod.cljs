(ns reactive-clojure.prod
  (:require [reactive-clojure.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)

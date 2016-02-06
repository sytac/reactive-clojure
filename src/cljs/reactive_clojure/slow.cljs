(ns reactive-clojure.slow
  (:require [reagent.core :as reagent :refer [atom]]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<! chan dropping-buffer]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def crsf-token
  (.-value (.getElementById js/document "__anti-forgery-token")))

(def counter (atom 0))

(defn counter-value
  []
  @counter)

(def clock (chan (dropping-buffer 1)))

(defn retrieve-counter []
  (go (let [response (<! (http/post "/count/slow" {:headers {"X-CSRF-Token" crsf-token}}))]
        (prn response))))

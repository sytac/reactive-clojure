(ns reactive-clojure.merge
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [reagent.core :as reagent :refer [atom]]
            [reactive-clojure.marbles-sandbox :as sandbox]
            [reactive-clojure.marbles :as marbles]
            [cljs.core.async :refer [put! chan <! onto-chan merge]]))

(def marbles (atom {:input-a [{:t 10 :l 1}
                              {:t 30 :l 2}
                              {:t 80 :l 3}]
                    :input-b [{:t 20 :l \A}
                              {:t 50 :l \B}]
                    :output []}))

(defn render []
  (let [input-a (chan)
        input-b (chan)
        output (chan)
        input (merge [input-a input-b])
        _ (onto-chan input-a (:input-a @marbles))
        _ (onto-chan input-b (:input-b @marbles))]
    (swap! marbles assoc :output [])
    (go (loop []
          (let [m (<! input)]
            (when m
              (.log js/console (str "got " m))
              (>! output m)
              (recur)))))

    (go (loop []
          (let [m (<! output)]
            (swap! marbles (fn [old]
                             (merge-with into old {:output [m]})))
            (recur))))))

(render)

(defn merged []
  (sandbox/marble-sandbox
   (sandbox/sandbox "inputA" (marbles/marbles-box (map (partial marbles/marble marbles render) (:input-a @marbles))))
   (sandbox/sandbox "inputB" (marbles/marbles-box (map (partial marbles/marble marbles render) (:input-b @marbles))))
   (sandbox/operator "(<! (merge (chan) (chan)))")
   (sandbox/sandbox "output" (marbles/marbles-box (map marbles/static-marble (:output @marbles))))))

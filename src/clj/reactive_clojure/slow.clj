(ns reactive-clojure.slow
  (:require [clojure.core.async :refer [go go-loop chan <! >! <!!]]))


; unbuffered input channel:
; - the first call to >! will succeed immediately
; - any following call to >! will park until the item is removed with a call to  <!
(def slow-counter-in  (chan))

; also an unbuffered output channel:
; - the first call to <! will park if there is no item in the channel
; - if there's a parked <!, execution proceeds after a call to >!
(def slow-counter-out (chan))

; constantly picking up an number from in chan
; introduce one second delay
; and putting the incremented number into the out chan
(go-loop []
  (let [in (<! slow-counter-in)]
    (Thread/sleep 1000)
    (>! slow-counter-out (inc in))
    (recur)))

(def counter
  (let [counter (atom 0)]
    (fn [request]
      {:body {:current (<!! (go (>! slow-counter-in @counter)
                           (reset! counter (<! slow-counter-out))))}})))

(ns bbb.body-parser
  (:import [com.petebevin.markdown MarkdownProcessor]))

(defn parse-text [text]
  (.markdown (MarkdownProcessor.) text))

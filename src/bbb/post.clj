(ns bbb.post
  (:import java.io.File
           [com.petebevin.markdown MarkdownProcessor])
  (:require [clj-yaml.core :as yaml]
            [clojure.string :as str]))

(defstruct post :title :permalink :body :year :month :day :layout)

(def post-defaults
  {:title     ""
   :permalink ""
   :body      ""
   :year      1969
   :month     12
   :day       31
   :layout    ""})

(defn to-md [text]
  (.markdown (MarkdownProcessor.) text))

(defn create-post [attrs]
  (merge post-defaults attrs
         {:title (str/replace (:title attrs) \- \ )
          :permalink (str "/" (:year attrs)
                          "/" (:month attrs)
                          "/" (:day attrs)
                          "/" (:title attrs) ".html")}))

(defn- get-files []
  (let [dir (File. "posts")
        children (.listFiles dir)]
    (filter #(.isFile %) children)))

(defn parse-post [^File post]
  (let [[file-name year month day title]
        (re-matches #"(?:.+\/)*(\d+)-(\d+)-(\d+)-(.*)(\.[^.]+)" (.getName post))]
    (create-post {:title title
                  :year  year
                  :month month
                  :day   day
                  :body  (to-md (slurp post))})))

(defn parse-posts []
  (let [files (get-files)]
    (map #(parse-post %) files)))


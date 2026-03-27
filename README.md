# Java Naive Bayes Sentiment Classifier

A Java implementation of a **Bernoulli Naive Bayes classifier** for binary sentiment analysis on movie reviews.

---

## Overview

This project implements a text classification system that predicts whether a movie review is **positive** or **negative**.

The classifier is based on the **Naive Bayes algorithm**, using:

* Document-level word presence (Bernoulli model)
* Laplace smoothing
* Vocabulary selection based on word frequency
* Standard evaluation metrics

---

## Features

* Train on labeled text datasets (positive/negative)
* Predict sentiment for unseen reviews
* Evaluate performance using:

  * Accuracy
  * Precision
  * Recall
  * F1 Score
* Configurable vocabulary size and filtering

---

## Project Structure

```text
src/
├── NaiveBayesClassifier.java   # Main classifier logic
├── EvaluationMetrics.java      # Metrics & confusion matrix
├── TextPreprocessor.java       # Tokenization & normalization
└── WordStats.java              # Word frequency tracking
```

---

## Input Data Format

The program expects reviews organized in directories:

```text
train/
├── pos/
└── neg/

test/
├── pos/
└── neg/
```

(There are samples files with a few positive and negative reviews for both training and testing)
(Add your own datasets if you want to test it)

* Each file = one review
* Files must contain plain text only

---

## How It Works

1. **Training Phase**

   * Reads all files from `train/pos` and `train/neg`
   * Builds word statistics based on document presence

2. **Vocabulary Selection**

   * Sorts words by frequency
   * Skips the most common words (optional)
   * Keeps top-N words as features

3. **Prediction**

   * Computes log-probabilities for each class
   * Uses Laplace smoothing
   * Chooses the class with the higher probability

4. **Evaluation**

   * Tests on unseen data
   * Generates confusion matrix and metrics

---

## Compilation

From the project root:

```bash
javac src\*.java
```

---

## Running the Program

```bash
java -cp src NaiveBayesClassifier train\pos train\neg test\pos test\neg
```

### Optional Parameters

```bash
java -cp src NaiveBayesClassifier train\pos train\neg test\pos test\neg 5500 15
```

| Parameter | Description                           |
| --------- | ------------------------------------- |
| 5500      | Maximum vocabulary size               |
| 15        | Number of most frequent words to skip |

---

## Example Results

Using 25,000 training reviews and 25,000 test reviews:

```text
Positive training documents: 12500
Negative training documents: 12500
Selected vocabulary size   : 5485

Confusion Matrix:
TP: 10410
TN: 10681
FP: 1819
FN: 2090

Accuracy : 84.36%
Precision: 0.8513
Recall   : 0.8328
F1 Score : 0.8419
```

---

## Notes

* This implementation follows the **Bernoulli Naive Bayes model**
* Each word is treated as **present or absent**, not by frequency
* Tokens are normalized by:

  * converting to lowercase
  * removing non-alphabetic characters

---

## Possible Improvements

* Stopword removal
* Stemming / lemmatization
* HTML tag cleaning
* N-gram features (bigrams, trigrams)
* TF-IDF weighting
* Comparison with Multinomial Naive Bayes

---

## Dataset

This project was tested on a movie review dataset consisting of:

* 12,500 positive reviews
* 12,500 negative reviews (training)
* 25,000 total test reviews

*(Dataset not included due to size)*

---

## Author

Agamemnon-Ioannis Spyrou




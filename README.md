# LuceneWebSearch
Implement Indexing and Searching for Cranfield Dataset using Lucene

# Build the jar with latest code
```mvn package```

# To run the application, preferred Analyzer and Similarity should be passed as System Arguments
## Supported Analyzers [EnglishAnalyzer, StandardAnalyzer, WhitespaceAnalyzer, CustomAnalyzer]
## Supported Similarity [Classic, LMDirichletSimilarity, BM25Similarity]
```java -jar target/LuceneWebSearch-0.0.1-SNAPSHOT.jar <Analyzer> <Similarity>```
# For Example
```java -jar target/LuceneWebSearch-0.0.1-SNAPSHOT.jar StandardAnalyzer BM25Similarity```

# For evaluation using trec_eval
```./trec_eval ../DataSet/QRelsCorrectedforTRECeval ../SearchResult/searching_<Analyzer>_<Similarity>/SearchResult.txt```
# For Example
```./trec_eval ../DataSet/QRelsCorrectedforTRECeval ../SearchResult/searching_StandardAnalyzer_BM25Similarity/SearchResult.txt```

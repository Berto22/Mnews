package com.example.android.minutenews.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.android.minutenews.data.DataSource
import com.example.android.minutenews.data.repository.NewsRepository
import com.example.android.minutenews.data.response.NewsResponse
import com.example.android.minutenews.db.NewsDao
import com.example.android.minutenews.db.newsEntity.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime


class NewsRepositoryImpl(
    private val newsDao: NewsDao,
    private val newsDataSource: DataSource
) : NewsRepository {

    init {
        //newsDao.deleteTopHeadlines()
        newsDataSource.loadNews.observeForever { it ->
            insertNews(it)
        }
    }
    override suspend fun getLatestNews(): LiveData<out List<Article>> {
        initRefresh()
        return withContext(Dispatchers.IO) {
            return@withContext newsDao.loadTopHeadlines()
        }
    }

    private fun insertNews(fetchedNews : NewsResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            //newsDao.deleteTopHeadlines()
            newsDao.insertTopHeadlines(fetchedNews.articles)
        }

    }

    private suspend fun initRefresh() {
        if (isRefresh(ZonedDateTime.now().minusMinutes(10))) {
            fetchRefreshNews()
            //10

        }

    }

    private suspend fun fetchRefreshNews() {
        newsDao.deleteTopHeadlines()
        newsDataSource.fetchNews("bbc-news")
    }

    private fun isRefresh(lastRefreshTime: ZonedDateTime) : Boolean {
        val fiveMinuteAgo = ZonedDateTime.now().minusMinutes(5)
        return lastRefreshTime.isBefore(fiveMinuteAgo)
        //5

    }
    /*private fun isRefresh(lastRefreshTime : LocalDateTime) : Boolean {
        val fiveMinutesAgo = LocalDateTime.now().minusMinutes(5)
        return lastRefreshTime.isBefore(fiveMinutesAgo)
    } */
}
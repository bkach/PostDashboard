/*
 * PostDashboard
 * Copyright (C) 2018 Boris Kachscovsky
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.example.boris.postdashboard.mocks

import com.example.boris.postdashboard.model.*

class MockModel {

    companion object {
        val mockPost = Post(1, 1, "Title", "Body")
        val mockUser = User(1, "hello")
        val mockComment = Comment(1, 1, "james@james.com", "body")
        val mockPhoto = Photo(1, "https://pbs.twimg.com/profile_images/413977145858220032/IiIfu9tE.png",
            "https://i.kym-cdn.com/entries/icons/original/000/013/564/doge.jpg")
        val mockMetadata = createMockPostsWithMetadata()

        private fun createMockPostsWithMetadata(): List<PostWithMetadata> {
            val post = PostWithMetadata()
            post.post = mockPost
            post.commentList = listOf(mockComment)
            post.photoList = listOf(mockPhoto)
            post.userList = listOf(mockUser)
            return listOf(post)
        }
    }


}

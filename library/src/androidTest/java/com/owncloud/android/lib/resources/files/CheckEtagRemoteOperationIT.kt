/* Nextcloud Android Library is available under MIT license
 *
 *   @author Tobias Kaminsky
 *   Copyright (C) 2021 Tobias Kaminsky
 *   Copyright (C) 2021 Nextcloud GmbH
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 *   BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 *   ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *   CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 *
 */
package com.owncloud.android.lib.resources.files

import com.owncloud.android.AbstractIT
import com.owncloud.android.lib.common.operations.RemoteOperationResult
import com.owncloud.android.lib.resources.files.model.RemoteFile
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckEtagRemoteOperationIT : AbstractIT() {
    @Test
    fun checkEtag() {
        val filePath = createFile("eTagFile")
        val remotePath = "/eTagFile.txt"
        assertTrue(
            @Suppress("Detekt.MagicNumber")
            UploadFileRemoteOperation(filePath, remotePath, "image/jpg", 1464818400)
                .execute(client).isSuccess
        )

        val readResult = ReadFileRemoteOperation(remotePath).execute(client)
        val remoteFile = readResult.data[0] as RemoteFile
        val eTag = remoteFile.etag

        var eTagResult = CheckEtagRemoteOperation(remotePath, eTag).execute(client)
        assertEquals(RemoteOperationResult.ResultCode.ETAG_UNCHANGED, eTagResult.code)

        eTagResult = CheckEtagRemoteOperation(remotePath, "wrongEtag").execute(client)
        assertEquals(RemoteOperationResult.ResultCode.ETAG_CHANGED, eTagResult.code)
    }
}

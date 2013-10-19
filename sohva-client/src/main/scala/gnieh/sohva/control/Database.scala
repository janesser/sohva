/*
* This file is part of the sohva project.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package gnieh.sohva
package control

import gnieh.sohva.async.{
  Database => ADatabase
}

import java.io.{
  File,
  InputStream
}

import net.liftweb.json.JObject

import scala.util.Try

import gnieh.diffson.JsonPatch

/** Gives the user access to the different operations available on a database.
 *  Among other operation this is the key class to get access to the documents
 *  of this database.
 *
 *  @author Lucas Satabin
 */
class Database private[sohva](wrapped: ADatabase) extends gnieh.sohva.Database {

  type Result[T] = Try[T]

  @inline
  val name = wrapped.name

  @inline
  val credit = wrapped.credit

  @inline
  val strategy = wrapped.strategy

  @inline
  def info: Try[Option[InfoResult]] =
    synced(wrapped.info)

  @inline
  def exists: Try[Boolean] =
    synced(wrapped.exists)

  def changes(filter: Option[String] = None): ChangeStream =
    wrapped.changes(filter)

  @inline
  def create: Try[Boolean] =
    synced(wrapped.create)

  @inline
  def delete: Try[Boolean] =
    synced(wrapped.delete)

  @inline
  def getDocById[T: Manifest](id: String, revision: Option[String] = None): Try[Option[T]] =
    synced(wrapped.getDocById(id, revision))

  @inline
  def getRawDocById(id: String, revision: Option[String] = None): Try[Option[String]] =
    synced(wrapped.getRawDocById(id, revision))

  @inline
  def getDocsById[T: Manifest](ids: List[String]): Try[List[T]] =
    synced(wrapped.getDocsById(ids))

  @inline
  def getDocRevision(id: String): Try[Option[String]] =
    synced(wrapped.getDocRevision(id))

  @inline
  def getDocRevisions(ids: List[String]): Try[List[(String, String)]] =
    synced(wrapped.getDocRevisions(ids))

  @inline
  def saveDoc[T <: IdRev: Manifest](doc: T): Try[Option[T]] =
    synced(wrapped.saveDoc(doc))

  @inline
  def saveDoc[T: Manifest](doc: T with Doc): Try[Option[T]] =
    synced(wrapped.saveDoc(doc))

  @inline
  def saveDocs[T](docs: List[T with Doc], all_or_nothing: Boolean = false): Try[List[DbResult]] =
    synced(wrapped.saveDocs(docs, all_or_nothing))

  @inline
  def copy(origin: String, target: String, originRev: Option[String] = None, targetRev: Option[String] = None): Try[Boolean] =
    synced(wrapped.copy(origin, target, originRev, targetRev))

  @inline
  def patchDoc[T <: IdRev: Manifest](id: String, rev: String, patch: JsonPatch): Try[Option[T]] =
    synced(wrapped.patchDoc(id, rev, patch))

  @inline
  def deleteDoc[T](doc: T with Doc): Try[Boolean] =
    synced(wrapped.deleteDoc(doc))

  @inline
  def deleteDoc(doc: IdRev): Try[Boolean] =
    synced(wrapped.deleteDoc(doc))

  @inline
  def deleteDocs(ids: List[String], all_or_nothing: Boolean = false): Try[List[DbResult]] =
    synced(wrapped.deleteDocs(ids, all_or_nothing))

  @inline
  def deleteDoc(id: String): Try[Boolean] =
    synced(wrapped.deleteDoc(id))

  @inline
  def attachTo(docId: String, file: File, contentType: String): Try[Boolean] =
    synced(wrapped.attachTo(docId, file, contentType))

  @inline
  def attachTo(docId: String, attachment: String, stream: InputStream, contentType: String): Try[Boolean] =
    synced(wrapped.attachTo(docId, attachment, stream, contentType))

  @inline
  def getAttachment(docId: String, attachment: String): Try[Option[(String, InputStream)]] =
    synced(wrapped.getAttachment(docId, attachment))

  @inline
  def deleteAttachment(docId: String, attachment: String): Try[Boolean] =
    synced(wrapped.deleteAttachment(docId, attachment))

  @inline
  def securityDoc: Try[SecurityDoc] =
    synced(wrapped.securityDoc)

  @inline
  def saveSecurityDoc(doc: SecurityDoc): Try[Boolean] =
    synced(wrapped.saveSecurityDoc(doc))

  def design(designName: String, language: String = "javascript"): Design =
    Design(wrapped.design(designName, language))

}

/*
 * (c) Copyright 2011 by Volker Bergmann. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, is permitted under the terms of the
 * GNU General Public License (GPL).
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED CONDITIONS,
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE
 * HEREBY EXCLUDED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package com.rapiddweller.jdbacl.model;

/**
 * Represents a database procedure.<br/><br/>
 * Created: 07.11.2011 16:06:00
 *
 * @author Volker Bergmann
 * @since 0.7.0
 */
public class DBProcedure extends AbstractDBObject {

  private static final long serialVersionUID = -7764571135020675359L;

  private String objectId;
  private String subProgramId;
  private String overload;

  /**
   * Instantiates a new Db procedure.
   *
   * @param name  the name
   * @param owner the owner
   */
  public DBProcedure(String name, DBPackage owner) {
    super(name, "procedure", owner);
    owner.addProcedure(this);
  }

  /**
   * Gets object id.
   *
   * @return the object id
   */
  public String getObjectId() {
    return objectId;
  }

  /**
   * Sets object id.
   *
   * @param objectId the object id
   */
  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  /**
   * Gets sub program id.
   *
   * @return the sub program id
   */
  public String getSubProgramId() {
    return subProgramId;
  }

  /**
   * Sets sub program id.
   *
   * @param subProgramId the sub program id
   */
  public void setSubProgramId(String subProgramId) {
    this.subProgramId = subProgramId;
  }

  /**
   * Gets overload.
   *
   * @return the overload
   */
  public String getOverload() {
    return overload;
  }

  /**
   * Sets overload.
   *
   * @param overload the overload
   */
  public void setOverload(String overload) {
    this.overload = overload;
  }

  @Override
  public boolean isIdentical(DBObject other) {
    if (this == other) {
      return true;
    }
    if (other == null || other.getClass() != getClass()) {
      return false;
    }
    DBProcedure that = (DBProcedure) other;
    return this.objectId.equals(that.objectId)
        && this.subProgramId.equals(that.subProgramId)
        && this.overload.equals(that.overload);
  }

}

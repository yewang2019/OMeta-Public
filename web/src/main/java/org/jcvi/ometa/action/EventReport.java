/*
 * Copyright J. Craig Venter Institute, 2013
 *
 * The creation of this program was supported by J. Craig Venter Institute
 * and National Institute for Allergy and Infectious Diseases (NIAID),
 * Contract number HHSN272200900007C.
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jcvi.ometa.action;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.jcvi.ometa.configuration.AccessLevel;
import org.jcvi.ometa.db_interface.ReadBeanPersister;
import org.jcvi.ometa.exception.ForbiddenResourceException;
import org.jcvi.ometa.exception.LoginRequiredException;
import org.jcvi.ometa.model.Project;
import org.jcvi.ometa.utils.Constants;
import org.jtc.common.util.property.PropertyHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: hkim
 * Date: 6/9/11
 * Time: 9:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventReport extends ActionSupport {
    private Logger logger = Logger.getLogger(EventReport.class);

    private ReadBeanPersister readPersister;

    private List<Project> projectList;
    private String projectNames;
    private List<String> projectNameList;
    private String fromDate;
    private String toDate;

    public EventReport() {
        readPersister = new ReadBeanPersister();
    }

    public String eventReport() {
        String returnValue = ERROR;

        try {
            projectNameList = new ArrayList<String>();
            if( projectNames == null || projectNames.equals( "" ))
                projectNameList.add("ALL");
            else if( projectNames.contains(","))
                projectNameList.addAll( Arrays.asList(projectNames.split(",")) );
            else
                projectNameList.add( projectNames );

            String userName = ServletActionContext.getRequest().getRemoteUser();
            projectList = readPersister.getAuthorizedProjects(userName, AccessLevel.View);
            // projectList = readPersister.getProjects( projectNameList );

            returnValue = SUCCESS;

        } catch ( ForbiddenResourceException fre ) {
            logger.info( Constants.DENIED_USER_VIEW_MESSAGE );
            addActionError( Constants.DENIED_USER_VIEW_MESSAGE );
            return Constants.FORBIDDEN_ACTION_RESPONSE;
        } catch( LoginRequiredException lre ) {
            logger.info( Constants.LOGIN_REQUIRED_MESSAGE );
            return LOGIN;
        } catch(Exception ex) {
            logger.error("Exception in Event Report Action : " + ex.toString());
            ex.printStackTrace();
        }

        return returnValue;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public String getProjectNames() {
        return projectNames;
    }

    public void setProjectNames(String projectNames) {
        this.projectNames = projectNames;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
}

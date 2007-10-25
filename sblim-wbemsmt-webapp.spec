%define name                    sblim-wbemsmt-webapp
%define version                 0.5.2
%define build_release           CVS
%define release                 %{build_release}jpp
%define section                 free

###############################################################################

%define wbemsmt_webapp_dir      %{_localstatedir}/lib/%{name}/webapps/%{name}

###############################################################################

Name:           %{name}
Version:        %{version}
Release:        %{release}
License:        Common Public License
Url:            http://sblim.sourceforge.net/
Group:          System Environment/Applications
Vendor:         IBM
Summary:        Provides a JSF-based task launcher and navigation tree for WBEM-SMT tasks
SOURCE0:        %{name}-%{version}-src.tar.bz2
BuildRoot:      %{_tmppath}/%{name}-%{version}-%{release}
BuildArch:      noarch

BuildRequires: jpackage-utils >= 1.5.32
BuildRequires: ant >= 1.6
BuildRequires: sblim-cim-client >= 1.3
BuildRequires: sblim-wbemsmt-commons => 0.5.0
BuildRequires: tomcat5-servlet-2.4-api >= 5.5.15
BuildRequires: jakarta-commons-lang >= 2.0
BuildRequires: jakarta-commons-collections >= 3.1
#BuildRequires: jakarta-commons-cli >= 1.0
#BuildRequires:  myfaces >= 1.1.5
#BuildRequires:  tomahawk >= 1.1.3
#BuildRequires:  xmlBeans >= 2.2.0

###############################################################################

Requires: jpackage-utils >= 1.5.32
Requires: sblim-cim-client >= 1.3
Requires: sblim-wbemsmt-commons >= 0.5.0
Requires: xerces-j2 >= 2.7.1
Requires: xalan-j2 >= 2.7.0
Requires: jakarta-commons-beanutils >= 1.7.0
Requires: jakarta-commons-codec >= 1.3
Requires: jakarta-commons-collections >= 3.1
Requires: jakarta-commons-digester >= 1.7
Requires: jakarta-commons-el >= 1.0
Requires: jakarta-commons-fileupload >= 1.0
Requires: jakarta-commons-lang >= 2.1
Requires: jakarta-commons-logging >= 1.0.4
Requires: jakarta-commons-validator >= 1.1.4
Requires: jakarta-taglibs-standard >= 1.1.1
Requires: tomcat5 >= 5.5.17
Requires: tomcat5-servlet-2.4-api >= 5.5.17
Requires: tomcat5-jsp-2.0-api >= 5.5.17
Requires: xml-commons >= 1.3.02
Requires: xml-commons-apis >= 1.3
Requires: xml-commons-resolver >= 1.1
Requires: struts >= 1.2.9
#Requires: jakarta-commons-cli >= 1.0
#Requires:  myfaces >= 1.1.5
#Requires:  tomahawk >= 1.1.3
#Requires:  xmlBeans >= 2.2.0

###############################################################################

%description
This module provides the Java Server Faces (JSF)- based task launcher and
navigation tree for all wbemsmt tasks. It also contains the admin console for
multi-login support. With the help of the webapp module, one can "snap in" a 
new task using the same infrastructure (launching, tree) mechanisms as the 
other tasks.


###############################################################################

%prep
%setup -q -n %{name}

###############################################################################

%build
CLASSPATH=$(build-classpath sblim-cim-client sblim-slp-client)
CLASSPATH=$(build-classpath tomcat5-servlet-2.4-api tomcat5-jsp-2.0-api):$CLASSPATH
CLASSPATH=$(build-classpath commons-collections commons-lang):$CLASSPATH
CLASSPATH=$(build-classpath xbean jsr173_1.0_api myfaces-api myfaces-impl tomahawk):$CLASSPATH
CLASSPATH=$(build-classpath sblim-wbemsmt/sblim-wbemsmt-commons sblim-wbemsmt/sblim-wbemsmt-commons-launcher-config):$CLASSPATH
export CLASSPATH

ant build-webapp


###############################################################################

%install
rm -rf $RPM_BUILD_ROOT
install -d $RPM_BUILD_ROOT%{_javadir}/sblim-wbemsmt
install -d $RPM_BUILD_ROOT%{_sysconfdir}/sblim-wbemsmt
install -d $RPM_BUILD_ROOT%{_sysconfdir}/sblim-wbemsmt/tasklauncher.d
install -d $RPM_BUILD_ROOT%{wbemsmt_webapp_dir}
install -d $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}

# Installation of documentation files
install AUTHORS   $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/AUTHORS
install ChangeLog $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/ChangeLog
install COPYING   $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/COPYING
install NEWS      $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/NEWS
install README    $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/README

install MultipleHostSupport     $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/MultipleHostSupport
install TroubleShooting         $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/TroubleShooting
install PortletContainerSupport $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/PortletContainerSupport
install SlpSupport              $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/SlpSupport
install StandaloneSupport       $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/StandaloneSupport

install target/package/etc/sblim-wbemsmt/%{name}.conf $RPM_BUILD_ROOT%{_sysconfdir}/sblim-wbemsmt/%{name}.conf

# Installation of WebApplication
mv target/package/%{name}/* $RPM_BUILD_ROOT/%{wbemsmt_webapp_dir}
install $RPM_BUILD_ROOT%{wbemsmt_webapp_dir}/WEB-INF/web.xml.standalone $RPM_BUILD_ROOT%{wbemsmt_webapp_dir}/WEB-INF/web.xml
install $RPM_BUILD_ROOT%{wbemsmt_webapp_dir}/WEB-INF/faces-config.xml.standalone $RPM_BUILD_ROOT%{wbemsmt_webapp_dir}/WEB-INF/faces-config.xml


###############################################################################

%post
unset JAVA_HOME
[ -r %{_sysconfdir}/tomcat5/tomcat5.conf ] && . %{_sysconfdir}/tomcat5/tomcat5.conf
[ -z "$CATALINA_HOME" ] && CATALINA_HOME=/var/lib/tomcat5
ln -sf %{wbemsmt_webapp_dir} $CATALINA_HOME/webapps/%{name}

[ -r %{_sysconfdir}/sblim-wbemsmt/sblim-wbemsmt-commons.conf ] && . %{_sysconfdir}/sblim-wbemsmt/sblim-wbemsmt-commons.conf
[ -z "$WBEMSMT_HELPDIR" ] && WBEMSMT_HELPDIR=/var/lib/sblim-wbemsmt/help
ln -sf $WBEMSMT_HELPDIR %{wbemsmt_webapp_dir}/help

[ -z "$JAVA_HOME" ] && [ -r %{_sysconfdir}/java/java.conf ] && . %{_sysconfdir}/java/java.conf
[ -z "$JAVA_HOME" ] && JAVA_HOME=%{_jvmdir}/java
build-jar-repository %{wbemsmt_webapp_dir}/WEB-INF/lib sblim-cim-client sblim-slp-client
build-jar-repository %{wbemsmt_webapp_dir}/WEB-INF/lib sblim-wbemsmt/sblim-wbemsmt-commons 
build-jar-repository %{wbemsmt_webapp_dir}/WEB-INF/lib sblim-wbemsmt/sblim-wbemsmt-commons-launcher-config
build-jar-repository %{wbemsmt_webapp_dir}/WEB-INF/lib commons-collections commons-lang commons-logging commons-digester 
build-jar-repository %{wbemsmt_webapp_dir}/WEB-INF/lib commons-beanutils commons-codec commons-el commons-fileupload commons-validator
build-jar-repository %{wbemsmt_webapp_dir}/WEB-INF/lib taglibs-core taglibs-standard
build-jar-repository %{wbemsmt_webapp_dir}/WEB-INF/lib tomcat5-servlet-2.4-api tomcat5-jsp-2.0-api servletapi5
build-jar-repository %{wbemsmt_webapp_dir}/WEB-INF/lib xerces-j2 xml-commons-resolver xml-commons-apis
build-jar-repository %{wbemsmt_webapp_dir}/WEB-INF/lib xalan-j2 xalan-j2-serializer 
build-jar-repository %{wbemsmt_webapp_dir}/WEB-INF/lib myfaces-api myfaces-impl tomahawk xbean jsr173_1.0_api
build-jar-repository %{wbemsmt_webapp_dir}/WEB-INF/lib struts
%{_bindir}/rebuild-gcj-db



###############################################################################

%preun
[ -r %{_sysconfdir}/tomcat5/tomcat5.conf ] && . %{_sysconfdir}/tomcat5/tomcat5.conf
[ -z "$CATALINA_HOME" ] && CATALINA_HOME=/var/lib/tomcat5
unlink $CATALINA_HOME/webapps/%{name}

unlink %{wbemsmt_webapp_dir}/help

if [ $1 = 0 ]; then
    find %{wbemsmt_webapp_dir}/WEB-INF/lib  \
         -name '\[*\]*.jar' \
         -not -type d | xargs rm -f
fi


###############################################################################

%files
%defattr(640,root,tomcat,0750)
%attr(644,root,root) %doc %{_docdir}/%{name}-%{version}/AUTHORS
%attr(644,root,root) %doc %{_docdir}/%{name}-%{version}/ChangeLog
%attr(644,root,root) %doc %{_docdir}/%{name}-%{version}/COPYING
%attr(644,root,root) %doc %{_docdir}/%{name}-%{version}/NEWS
%attr(644,root,root) %doc %{_docdir}/%{name}-%{version}/README
%attr(644,root,root) %doc %{_docdir}/%{name}-%{version}/MultipleHostSupport
%attr(644,root,root) %doc %{_docdir}/%{name}-%{version}/TroubleShooting
%attr(644,root,root) %doc %{_docdir}/%{name}-%{version}/PortletContainerSupport
%attr(644,root,root) %doc %{_docdir}/%{name}-%{version}/SlpSupport
%attr(644,root,root) %doc %{_docdir}/%{name}-%{version}/StandaloneSupport
%dir %{_sysconfdir}/sblim-wbemsmt/tasklauncher.d
%attr(664,root,root) %config(noreplace) %{_sysconfdir}/sblim-wbemsmt/%{name}.conf
%{wbemsmt_webapp_dir}/*

###############################################################################
%changelog
* Thu Oct 25 2007 Michael Bauschert <michael.bauschert@de.ibm.com> 0.5.2-CVS
  - Inclusion of fixes for the following issues:
    o 1819758 wbemsmt-webapp: wrong xml :overwrite local conf with slp
	o 1813968 wbemsmt-webapp: utils for managing open ports
	o 1813970 wbemsmt-webapp: update the tree if requested from BL
	
* Thu Sep 13 2007 Michael Bauschert <michael.bauschert@de.ibm.com> 0.5.2-1
  - Inclusion of fixes for the following issues:
    o 1793932 wbemsmt-webapp: noborder stylesheet class for main.css
    o 1784067 wbemsmt-webapp: rework admin panels
    
* Mon Aug 27 2007 Wolfgang Taphorn <taphorn@de.ibm.com> 0.5.1-1
  - Inclusion of fixes for the following issues:
    o 1780048  wbemsmt-webapp: add protocol to login settings
    o 1775196  wbemsmt-webapp: confugurable namespace for embed login
    o 1775191  wbemsmt-webapp: stop ajax requests before a page request
    o 1774772  wbemsmt-webapp: remove application namespace
    o 1773221  wbemsmt-webapp: add configurable properties to tasks
    o 1768402  wbemsmt-webapp: remove dummy-config for not-configured task

* Fri Jul 6 2007 Wolfgang Taphorn <taphorn@de.ibm.com> 0.5.0-1
  - Inclusion of fixes for the following issues:
    o 1756335  wbemsmt-webapp: synchronize concurrent ajax requests
    o 1754931  wbemsmt-webapp: Upgrade to build environment
    o 1746923  wbemsmt-webapp: docu for slp config/usage
    o 1746585  wbemsmt-admin: namespace for application
    o 1740803  wbemsmt-slp: use treeconfig to check for slp hosts
    o 1737773  wbemsmt-jsf: Save Data at login
    o 1737049  wbemsmt-jsf: parallel logon for multiple hosts
    o 1731237  wbemsmt-jsf: store updateInterval in Cookies
    o 1731236  wbemsmt-jsf: childrenTables as HtmlDataTable
    o 1728152  wbemsmt-common: priority for eventlisteners
    o 1727285  wbemsmt-jsf: configurable ajax update interval
    o 1728151  wbemsmt-jsf: scrollable childTables
    o 1724637  wbemsmt-common: InstanceNaming based in FCO
    o 1724664  wbemsmt-jsf: fixed with for tabs in tabbedPanel
    o 1724636  wbemsmt-common: add Session objec
    o 1707399  wbemsmt: change file encoding to utf8
    o 1704409  wbemsmt:dynamically build tree node by the usage of a class
    o 1674057  wbemsmt: update to sblim-cimclient-1.3.2
    o 1671504  wbemsmt-webapp: update to myfaces-core-1.1.5
    o 1699791  wbemsmt-webapp: revert changes after selecting new treenode
    o 1681993  wbemsmt-common: new UITypes
    o 1680547  wbemsmt-webapp: welcome screens
    o 1680566  wbemsmt-admin: feedback messages
    o 1675864  wbemsmt-webapp: common contextMenu
    o 1671545  wbemsmt-common: version-attribute for treeConfig.xml
    o 1658315  wbemsmt-webapp help integration
    o 1655037  wbemsmt-webapp: after failed login old treeConfig is shown
    o 1655036  wbemsmt-webapp: ajax integration
    o 1652247  wbemsmt-webapp display only requested tasks in embeddedMode
    o 1648068  wbemsmt-webapp: Login for Portlet containers
    o 1648024  wbemsmt-webapp: confirmation at pending changes
    o 1648025  wbemsmt-webapp: hide help  and logout
    o 1645083  wbemsmt-webapp: multilineTable - row deletion
    o 1638601  wbemsmt-webapp: remove update link
    o 1638397  wbemsmt-webapp: add action menue
    o 1638396  wbemsmt-webapp: add close link to wait indicator
    o 1636031  wbemsmt-webapp: theme for SourceForge
    o 1634187  wbemsmt-webapp: Rework AdminConsole&WelcomePage
    o 1631455  wbemsmt-webapp: prompt while cancelling a wizard
    o 1620646  wbemsmt-webapp: indicator for longrunning actions
    o 1619945  wbemsmt-webapp: TreeSynchronisation
    o 1619423  wbemsmt-webapp: rework ui: field indicators
    o 1619411  wbemsmt-webapp: rework ui: changes for IE,opera...
    o 1618646  wbemsmt-webapp: rework ui: messages
    o 1615822  wbemsmt-webapp: rework ui: wizards
    o 1613959  wbemsmt-webapp: rework ui: content of editPanels
    o 1613893  wbemsmt-webapp: rework UI: L&F Tree and EditPanels
    o 1613888  Rework UI: Logon Panel

* Mon Dec 4 2006 Wolfgang Taphorn <taphorn@de.ibm.com> 0.2.3-1
  - Inclusion of fixes for the following bug entries:
    o 1609070 wbemsmt-webapp: admin console
  - Upgrade for documentation
* Tue Oct 17 2006 Wolfgang Taphorn <taphorn@de.ibm.com> 0.2.2-1
  - Inclusion of fixes for the following bug entries:
    o 1575096  Tasklauncher: NPE switching from AdminConsole to Login
    o 1574472  Cleanup for client projects
    o 1573746  Preset handling for Login
* Sat Jul 22 2006 Wolfgang Taphorn <taphorn@de.ibm.com> 0.2.1-1
  - Initial upload


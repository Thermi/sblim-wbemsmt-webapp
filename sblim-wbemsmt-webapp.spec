%define name                    sblim-wbemsmt-webapp
%define version                 0.2.3
%define build_release           1
%define release                 %{build_release}jpp
%define section                 free

###############################################################################

%define tomcat_webapp_dir       /var/lib/tomcat5/webapps/
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
BuildRequires: sblim-cim-client >= 1.3.2
BuildRequires: sblim-wbemsmt-commons => 0.2.3
BuildRequires: tomcat5-servlet-2.4-api >= 5.5.15
BuildRequires: jakarta-commons-cli >= 1.0
BuildRequires: jakarta-commons-lang >= 2.0
BuildRequires: jakarta-commons-collections >= 3.1
#BuildRequires:  myfaces >= 1.1.5
#BuildRequires:  tomahawk >= 1.1.3
#BuildRequires:  xmlBeans >= 2.2.0

###############################################################################

Requires: jpackage-utils >= 1.5.32
Requires: sblim-cim-client >= 1.3.2
Requires: sblim-wbemsmt-commons >= %{wbemsmt_commons_version}
Requires: xerces-j2 >= 2.7.1
Requires: xalan-j2 >= 2.7.0
Requires: jakarta-commons-beanutils >= 1.7.0
Requires: jakarta-commons-cli >= 1.0
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
CLASSPATH=$(build-classpath tomcat5-servlet-2.4-api):$CLASSPATH
CLASSPATH=$(build-classpath commons-collections commons-lang):$CLASSPATH
CLASSPATH=$(build-classpath xbean jsr173_1.0_api myfaces-api myfaces-impl tomahawk):$CLASSPATH
CLASSPATH=$(build-classpath sblim-wbemsmt/sblim-wbemsmt-commons sblim-wbemsmt/sblim-wbemsmt-commons-launcher-config):$CLASSPATH
export CLASSPATH

ant build-webapp


###############################################################################

%install
rm -rf $RPM_BUILD_ROOT
install -d $RPM_BUILD_ROOT%{_javadir}/sblim-wbemsmt
install -d $RPM_BUILD_ROOT%{wbemsmt_webapp_dir}
install -d $RPM_BUILD_ROOT%{tomcat_webapp_dir}
install -d $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}

# Installation of documentation files
install AUTHORS   $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/AUTHORS
install ChangeLog $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/ChangeLog
install COPYING   $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/COPYING
install NEWS      $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/NEWS
install README    $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/README

install MultipleHostSupport  $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/MultipleHostSupport
install TroubleShooting      $RPM_BUILD_ROOT%{_docdir}/%{name}-%{version}/TroubleShooting

# Installation of WebApplication
mv target/package/%{name}/* $RPM_BUILD_ROOT/%{wbemsmt_webapp_dir}
(
  cd $RPM_BUILD_ROOT/%{tomcat_webapp_dir} &&
    ln -s %{wbemsmt_webapp_dir} %{name}
)


###############################################################################

%post
unset JAVA_HOME
[ -r %{_sysconfdir}/tomcat5/tomcat5.conf ] && . %{_sysconfdir}/tomcat5/tomcat5.conf
[ -z "$JAVA_HOME" ] && [ -r %{_sysconfdir}/java/java.conf ] && \
    . %{_sysconfdir}/java/java.conf
[ -z "$JAVA_HOME" ] && JAVA_HOME=%{_jvmdir}/java
build-jar-repository %{wbemsmt_webapp_instdir}/WEB-INF/lib sblim-wbemsmt/sblim-wbemsmt-commons 
build-jar-repository %{wbemsmt_webapp_instdir}/WEB-INF/lib sblim-wbemsmt/sblim-wbemsmt-commons-launcher-config
build-jar-repository %{wbemsmt_webapp_instdir}/WEB-INF/lib commons-collections commons-lang commons-logging commons-digester 
build-jar-repository %{wbemsmt_webapp_instdir}/WEB-INF/lib commons-beanutils commons-codec commons-el commons-fileupload commons-validator
build-jar-repository %{wbemsmt_webapp_instdir}/WEB-INF/lib taglibs-core taglibs-standard
build-jar-repository %{wbemsmt_webapp_instdir}/WEB-INF/lib tomcat5-servlet-2.4-api servletapi5
build-jar-repository %{wbemsmt_webapp_instdir}/WEB-INF/lib xerces-j2 xml-commons-resolver xml-commons-apis
build-jar-repository %{wbemsmt_webapp_instdir}/WEB-INF/lib xalan-j2 xalan-j2-serializer 
build-jar-repository %{wbemsmt_webapp_instdir}/WEB-INF/lib myfaces-api myfaces-impl tomahawk xbean jsr173_1.0_api
build-jar-repository %{wbemsmt_webapp_instdir}/WEB-INF/lib struts
%{_bindir}/rebuild-gcj-db



###############################################################################

%preun
[ -r %{_sysconfdir}/tomcat5/tomcat5.conf ] && . %{_sysconfdir}/tomcat5/tomcat5.conf
unlink $CATALINA_HOME/webapps/%{wbemsmt_webapp}
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
%{wbemsmt_webapp_dir}/*
%{tomcat_webapp_dir}/*

###############################################################################
%changelog
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


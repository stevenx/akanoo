<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>Akanoo Note Taking</title>
<meta name="gwt:property" content="locale=${message(code:'page.locale',default:'en')}">
<meta name="viewport"
	content="target-densityDpi=device-dpi, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">

<script type="text/javascript">
	window.gwtAtmosphereUrl = "${gwtAtmosphereUrl}";
	window.gwtRealName = "${gwtRealName}";
	window.gwtLogoutLink = "${gwtLogoutUrl}";
</script>
<script type="text/javascript"
	src="${resource(plugin: "akanoo-core", dir: 'gwt/akanoo', file: 'akanoo.nocache.js')}"></script>
<link rel="icon" href="${resource(dir: 'images', file: 'favicon.ico')}"
	type="image/x-icon" />
<link rel="shortcut icon"
	href="${resource(plugin: "akanoo-core",dir: 'images', file: 'favicon.ico')}"
	type="image/x-icon" />
</head>

<body>
	<iframe id="__gwt_historyFrame" style="width: 0; height: 0; border: 0"></iframe>
	<g:if env="production">
		<g:render template="feedback" />
	</g:if>
</body>
</html>


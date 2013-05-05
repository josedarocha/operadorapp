
![](https://github.com/TactilApp/OperadorApp/raw/master/Recursos/00%20-%20icono%20y%20default/Icon%402x.png) OperadorApp
=======================
OperadorApp sirve para descubrir de qué compañía es un número de teléfono móvil (de momento únicamente en España).

Se puede descargar gratuítamente desde Google Play: [OperadorApp](http://play.google.com/store/apps/details?id=com.tactilapp.operadorapp).

Instalación
===========
A continuación se explican los pasos que necesitas para probar la aplicación en tu propio ordenador.

Clonado
-------
	git clone git@github.com:TactilApp/OperadorApp_android.git

Submódulos
---------
La aplicación utiliza la relación entre cadenas de la CMT y operadoras proporcionado por el módulo OperadorApp-Companies (https://github.com/TactilApp/OperadorApp-Companies), para enlazarlo:

	git submodule init
	git submodule update
	
y después se mueve el fichero necesario de este módulo (companias/companies-color.plist) a la carpeta de XMLs de la aplicación (res/xml), cambiándole el guión medio por guión bajo para evitar el problema de nomenclatura en Android:

	mv "companias/companies-color.plist" "res/xml/companies_color.plist"
	
Ahora, si quieres, se puede eliminar la carpeta "companias" donde se ha descargado el módulo.

Configuración
-------------
Por último, la aplicación emplea UrbanAirship para notificaciones, Flurry para estadísticas y Admob para publicidad.

    - Urban Airship
	   Para configurarlo, hay que modificar el fichero assets/airshipconfig.properties con las claves que te sean asignadas. Mientras que estas valgan "ejemplo" el sistema de notificaciones quedará desactivado.

    - Flurry
	   Para configurarlo, hay que modificar en el fichero strings.xml la clave flurry_id. Mientras que esta valga "ejemplo" el sistema quedará desactivado.

	- Admob
	   Para configurarlo, hay que modificar en el fichero strings.xml la clave admob_unit_id. Mientras que esta valga "ejemplo" el sistema quedará desactivado.
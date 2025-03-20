from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.screenmanager import ScreenManager, Screen
from kivy.lang import Builder
from kivy.clock import Clock
from plyer import gps
from kivy.utils import get_color_from_hex
from kivy.factory import Factory

#Definir Colores
COLOR_PRIMARY = get_color_from_hex('#1F1B4D')
COLOR_SECONDARY = get_color_from_hex('#FF5858')

#Codigo Base
#Cargar el archivo .kv
Builder.load_file('urbango.kv')

class UrbanGoScreenManager(ScreenManager):
    pass

class InicioScreen(Screen):
    pass

class RutasScreen(Screen):
    pass

class UrbanGoApp(App):
    def build(self):
        self.title = 'UrbanGo'
        return UrbanGoScreenManager()
    
if __name__ == '__main__':
    UrbanGoApp().run()

#Base de datos SQLite (Modo Ofline)
import sqlite3

class DatabaseManager:
    def __init__(self, db_name='database/urbango.db'):
        self.conn = sqlite3.connect(db_name)
        self.cursor = self.conn.cursor()
        self._create_tables()

    def _create_tables(self):
        #Tabla de rutas
        self.cursos.execute('''
            CREATE TABLE IF NOT EXISTS rutas (
                id INTEGRER PRIMARY KEY,
                nombre TEXT.
                circuito TEXT'
            )
        ''')
        self.conn.commit()

#Inicializar la base de datos
db = DatabaseManager()

#Datos de ejemplo
db.cursor.execute("INSERT INTO rutas (nombre, circuito) VALUES ('Ruta 25', 'Rojo')")
db.conn.commit()

#Cargar rutas desde la base de datos
class UrbanGo(App):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.rutas = self.load_rutas()

    def load_rutas(self):
        db.cursor.execute("SELECT * FROM rutas")
        return db.cursor.fetchall()
    
#Integracion de menu
class UrbanGoApp(App):
    def open_menu(self):
        menu = Factory.MenuLateral()
        self.root.add_widget(menu)

#Geolocalizacion
class InicioScreen(Screen):
    def on_enter(self):
        gps.configure(on_location=self.update_ubication)
        gps.start()

    def update_ubication(self, **kwargs):
        lat = kwargs.get('tal')
        lon = kwargs.get('lon')
        print(f"Ubicacion actual: {lat}, {lon}")
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import domain.Cars;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 *
 * @author faubricioch
 */
public class CarsFile {
    //creacion de atributos
    private RandomAccessFile randomAccessFile;
    private int regsQuantity;
    private int regsSize;
    private String myFilePath;
    
    public CarsFile(File file) throws IOException{
        //almaceno ruta del file
        myFilePath=file.getPath();
        
        //indico el tamaño del registro
        this.regsSize=53;
        
        //validar la existencia del file y si en realidad es un file
        if(file.exists() && !file.isFile()){
            throw new IOException(file.getName()+ " es inválido");
        }else{
            //instanciamos el RAF
            randomAccessFile=new RandomAccessFile(file, "rw");
            
            //indicamos la cantidad de registros del archivo
            this.regsQuantity=(int)Math.ceil((double)randomAccessFile.length() / 
                    (double)regsSize);
        }//fin if de validacion
    }//fin del constructor
    /*
    Se guardo la ruta del file
    Se indica el tamaño máximo de cada registro
    Se valida la existencia del file y si es un file
    Se instancia el RAF que recibe el file y la indicacion Read y Write
    Se indica la cantidad de registros que posee el archivo file
    */
    
    
    //cierre de archivos usados
    public void close() throws IOException{
        randomAccessFile.close();
    }//Se cierra el RAF usado
    
    //indicamos la cantidad de registros presentes en el file
    public int fileSize(){
        return this.regsQuantity;
    }
    
    //insertar nuevo auto putValueBySerie según serie
    public void putValueBySerie(int serie, Cars car)throws IOException{
        Cars useCar=new Cars();
        //declaro for para buscar la posicion del auto
        for(int i=0; i<this.regsQuantity;i++){
            //coloco el puntero en posicio i
            randomAccessFile.seek(i*this.regsSize);
            
            //hacemos la lectura para encontrar al registro con la serie correcta
            useCar.setName(randomAccessFile.readUTF());
            useCar.setYear(randomAccessFile.readInt());
            useCar.setMileage(randomAccessFile.readFloat());
            useCar.setAmerican(randomAccessFile.readBoolean());
            useCar.setSerie(randomAccessFile.readInt());
            
            if(useCar.getSerie()==serie){
                //colocamos el puntero en la posición original
                randomAccessFile.seek(i*this.regsSize);
                
                //hacemos la escritura correspondiente
                randomAccessFile.writeUTF(car.getName());
                randomAccessFile.writeInt(car.getYear());
                randomAccessFile.writeFloat(car.getMileage());
                randomAccessFile.writeBoolean(car.isAmerican());
                randomAccessFile.writeInt(car.getSerie());
            }
            
        }//fin for busca serie
    }
    
    //insertar nuevo registro en una posición requerida
    public boolean putValue(int position, Cars car) throws IOException{
        //se verifica si la inserción es válida
        if(position<0 && position>this.regsQuantity){
            System.err.println("1. Position out of bounds");
            return false;
        }else{//valida que la posicion sea correcta
            if(car.sizeInBytes()>this.regsSize){
                System.err.println("2. Size out of bounds");
                return false;
            }else{//valida que el tamaño del registro sea correcto
                //todo es correcto
                randomAccessFile.seek(position * this.regsSize);
                randomAccessFile.writeUTF(car.getName());
                randomAccessFile.writeInt(car.getYear());
                randomAccessFile.writeFloat(car.getMileage());
                randomAccessFile.writeBoolean(car.isAmerican());
                randomAccessFile.writeInt(car.getSerie());
                return true;
            }
        }//valida posicion correcta
    }//fin metodo putValue
    /*
    Se insertará un nuevo registro en una posición deseada
        Se verifica que la posicion sea valida 
        Se verifica que el tamaño del registro sea correcto
        Se coloca el seek en la posicion deseada y se escriben los datos
    */
    
    //insertar registro al final del archivo
    public boolean addEndRecord(Cars car) throws IOException{
        boolean success = putValue(this.regsQuantity, car);
        if(success){
            ++this.regsQuantity;
        }
        return success;
    }//fin metodo para agregar registros al final
    
    //creo metodo getCarBySerie
    public Cars getCarBySerie(int serie) throws IOException{
        //creo el objeto
        Cars carTemp=new Cars();
        
        //if
        if(serie!=0 && serie>0){
            //for para busqueda por serie
            for(int i=0; i<this.regsQuantity; i++){
                //colocamos el buscador en posicion i
                randomAccessFile.seek(i*this.regsSize);

                //hacemos la lectura para encontrar al registro
                carTemp.setName(randomAccessFile.readUTF());
                carTemp.setYear(randomAccessFile.readInt());
                carTemp.setMileage(randomAccessFile.readFloat());
                carTemp.setAmerican(randomAccessFile.readBoolean());
                carTemp.setSerie(randomAccessFile.readInt());

                if(carTemp.getSerie()==serie){
                    return carTemp;
                }
            }
        }else{
            return null;
        }
        return carTemp;
    }
    
    //obtener un registro en específico
    public Cars getCar(int position)throws IOException{
        //validamos que la posición sea correcta
        if(position>=0 && position<=this.regsQuantity){
            //colocamos el puntero o buscador donde corresponde
            randomAccessFile.seek(position * this.regsSize);
            
            //Hacemos la lectura correspondiente
            Cars carTemp=new Cars();
            carTemp.setName(randomAccessFile.readUTF());
            carTemp.setYear(randomAccessFile.readInt());
            carTemp.setMileage(randomAccessFile.readFloat());
            carTemp.setAmerican(randomAccessFile.readBoolean());
            carTemp.setSerie(randomAccessFile.readInt());
            //preguntamos su existencia
            if(carTemp.getSerie()==0){
                return null;
            }else{
                return carTemp;
            }//pregunta su existencia
        }else{
            System.err.println("3. Position out of bounds");
            return null;
        }//validacion de posicion correcta
    }//fin metodo getcar
    /*
    Se crea el metodo para obtener un registro específico
        Se valida que la posicion sea correcta
        Nos ubicamos en la posicion requerida
        Instanciamos un objeto y lo llenamos con los daton necesarios haciendo una lectura
        Preguntamos su existencia
    */
    
    //eliminamos un registro en específico
    public boolean deleteCar(int serie) throws IOException{
        //creamos un objeto
        Cars myCar;
        
        //buscamos el registro específico
        for(int i=0; i<this.regsQuantity; i++){
            //obtener al estudiante con la posición i
            myCar=this.getCar(i);
            //preguntar si es el auto que quiero eliminar
            if(myCar==null){
                return false;
            }
            if(myCar.getSerie()==serie){
                myCar.setSerie(0);
                return this.putValue(i, myCar);
            }
        }//for de busqueda
        return false;
    }//fin metodo para eliminar un registro
    /*
    Se crea el metodo para eliminar registros
        Se crea un objeto de tipo Cars
        Se crea un for para buscar el registro específico
        Se obtiene un carro llamando a getCar en posicion i
        Preguntamos si es el estudiante correcto
        Le damos a serie el valor de 0
        Enviamos la posicion y el objeto a putValue para que actualice el registro
    */
    
    //retornar una lista de carros (ArrayList)
    public ArrayList<Cars> getAllCars() throws IOException{
        //instanciamos un Array list de tipo Cars
        ArrayList<Cars> carsArray=new ArrayList<Cars>();
        
        //llamamos a todos los registros
        for(int i=0; i<regsQuantity; i++){
            //obtenemos el registro y objeto en posicion i
            Cars cTemp= this.getCar(i);
            
            //preguntamos sobre el registro obtenido
            if(cTemp != null){
                //llenamos el array con los registros obtenidos
                carsArray.add(cTemp);
            }//if de llenado de array
        }//for de llamado

        return carsArray;
    }//fin metodo para retornar lista de arrays
    /*
    Se crea el metodo para retornar una lista de carros
    Se instancia un ArrayList de tipo Cars
    Se llaman a todos los registros 1 x 1 
    Preguntamos si la informacion es valida y 
        llenamos el array con la informacion correcta
    */
    
    public void compressFile(ArrayList<Cars> cars) throws IOException{
        //tamaño del Array
        int arraySize=cars.size();
        
        //creo un objeto para enviarlo
        Cars myCar=new Cars();
        for(int i=0; i<arraySize; i++){
            myCar.setName(cars.get(i).getName());
            myCar.setYear(cars.get(i).getYear());
            myCar.setMileage(cars.get(i).getMileage());
            myCar.setAmerican(cars.get(i).isAmerican());
            myCar.setSerie(cars.get(i).getSerie());
            putValue(i, myCar);
        }//escribirá el archivo
    }
}

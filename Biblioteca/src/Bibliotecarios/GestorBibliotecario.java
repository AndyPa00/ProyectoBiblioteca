package Bibliotecarios;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class GestorBibliotecario {
	LocalDate fecha = LocalDate.now();
	public static int idISBN=10000;

	/* Libro:
	 * isbn		int
	 * Titulo	String
	 * Autor	String
	 * Prestado	(true/false) boolean
	 * fecha	(null/fecha) String (dd-mm-yyyy)
	 */

	public boolean existeElLibro(BufferedReader in, String title) {
		String c, titulo;
		boolean existe=false;
		
		try {
			in = new BufferedReader(new FileReader("libros"));
			
			while ((c = in.readLine()) != null && !existe) {
				String todo;						 					// isbn+titulo+autor+prestado+fecha
				
				todo = c.substring(c.indexOf(",") + 1); 				// titulo+autor+prestado+fecha
				titulo = todo.substring(0, todo.indexOf(",")); 			// titulo
				
				if(titulo.equals(title)) {
					existe=true;
				}
			}
			
		}catch (FileNotFoundException e) {
			System.err.println("Fichero no encontrado!!");
		}catch (IOException f) {
			System.out.println();
		}finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error, " + e.getMessage());
				}
			}
		}
		return existe;
	}

	public void sumaidISBN() {
		GestorBibliotecario.idISBN++;
	}

	public boolean darDeAlta(BufferedReader in, PrintWriter out, String title) { // Devuelve true cuando se le da de alta
		boolean noExiste=true;
		
		if(!existeElLibro(in, title)) {
			noExiste=false;
		}
		
		if(!noExiste) {
			return !noExiste;
		}
		
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter("libros",true)));
			int isbnEs=idISBN;
			String autorEs=GestorBibliotecario.nombres()+" "+GestorBibliotecario.apellidos();
			String tituloEs=title;
			boolean prestadoEs=false;
			String fechaEs = "NO";
			/*
			String fechaEs = fecha.toString();				// yyyy-mm-dd
			String dia, mes, anno;
			dia=fechaEs.substring(fechaEs.indexOf("-")+1);	// mm-dd
			dia=dia.substring(dia.indexOf("-")+1);				// dd
			mes=fechaEs.substring(fechaEs.indexOf("-")+1);	// mm-dd
			mes=mes.substring(0, mes.indexOf("-")+1);			// mm
			anno=fechaEs.substring(0, fechaEs.indexOf("-"));	// yyyy
			*/
			out.println(isbnEs+","+tituloEs+","+autorEs+","+prestadoEs+","+fechaEs);
			this.sumaidISBN();
		} catch (FileNotFoundException e) {
			System.err.println("Fichero no encontrado!");
		}catch (IOException f) {
			System.out.println();
		}finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error, " + e.getMessage());
				}
			}
		}
		return !noExiste;
	}
	
	public boolean darDeBaja(BufferedReader in, PrintWriter out, String title) {
		boolean encontrado=false;
		int  veces2=0;
		String c, titulo="sin titulo";
		
		if(existeElLibro(in, title)) {
			encontrado=true;
		}
		
		if(!encontrado) {
			return encontrado;
		}

		try {
			in = new BufferedReader(new FileReader("libros"));
			out = new PrintWriter(new BufferedWriter(new FileWriter("librosTemporal")));
			
			while ((c = in.readLine()) != null) {
				String todo;						 					// isbn+titulo+autor+prestado+fecha
				
				todo = c.substring(c.indexOf(",") + 1); 				// titulo+autor+prestado+fecha
				titulo = todo.substring(0, todo.indexOf(",")); 			// titulo
				
				if(titulo.equals(title)) {
					veces2++;
				}else {
					out.println(c);
				}
			}
			
		}catch (FileNotFoundException e) {
			System.err.println("Fichero no encontrado!!");
		}catch (IOException f) {
			System.out.println();
		}finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error, " + e.getMessage());
				}
			}
			if (out != null) {
				out.close();
			}
			GestorBibliotecario.renombrarlo();
		}
		
		if(veces2==0) {
			System.out.println("Se ha borrado el ejemplar de "+title);
		}else {
			System.out.println("Se han borrado los ejemplares del libro"+title);
		}
		
		return encontrado;
	}

	public boolean modificarTitulo(BufferedReader in, PrintWriter out, String tituloComparar, String tituloNuevo) {
		
		if(!existeElLibro(in, tituloComparar)) {
			return false;
		}
		String c, titulo;
			
		try {
			in = new BufferedReader(new FileReader("libros"));		
			out = new PrintWriter(new BufferedWriter(new FileWriter("librosTemporal")));	
			
			while ((c = in.readLine()) != null) {

				String todo; 										// isbn+titulo+autor+prestado+fecha
				todo = c.substring(c.indexOf(",") + 1); 			// titulo+autor+prestado+fecha
				titulo = todo.substring(0, todo.indexOf(",")); 		// titulo

				if (titulo.equals(tituloComparar)) {
					String autorEs, prestadoEs, fechaEs, isbnEs;
					isbnEs = c.substring(0, c.indexOf(","));				 		// titulo
					autorEs = c.substring(c.indexOf(",") + 1); 						// titulo+autor+prestado+fecha
					autorEs = autorEs.substring(autorEs.indexOf(",") + 1); 			// autor+prestado+fecha
					autorEs = autorEs.substring(0, autorEs.indexOf(",")); 			// autor
					prestadoEs = c.substring(c.indexOf(",") + 1); 					// titulo+autor+prestado+fecha
					prestadoEs = prestadoEs.substring(prestadoEs.indexOf(",") + 1); // autor+prestado+fecha
					prestadoEs = prestadoEs.substring(prestadoEs.indexOf(",") + 1); // prestado+fecha
					prestadoEs = prestadoEs.substring(0, autorEs.indexOf(",")); 	// prestado
					fechaEs = c.substring(c.lastIndexOf(",") + 1);					// fecha

					out.println(isbnEs + "," + tituloNuevo + "," + autorEs + "," + prestadoEs + "," + fechaEs);
				} else {
					out.println(c);
				}
			}

		} catch (FileNotFoundException e) {
			System.err.println("Fichero no encontrado!!");
		} catch (IOException f) {
			System.out.println();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					System.out.println("Error, " + e.getMessage());
				}
			}
		}
		GestorBibliotecario.renombrarlo();
		return true;
	}
	
	public static void renombrarlo(){
		Path partida = FileSystems.getDefault().getPath("librosTemporal");
		Path destino = FileSystems.getDefault().getPath("libros");
		 try {
			 Files.move(partida, destino, StandardCopyOption.REPLACE_EXISTING);
		 } catch (IOException e) {
			 System.out.println("Errrooooorrr"+e.getMessage());
		 }
	}
	
	private static String nombres() {
		String nombre[]=new String[36];
		nombre[0]="Alfonso";
		nombre[1]="Lucia";
		nombre[2]="Emilio";
		nombre[3]="Sofia";
		nombre[4]="Antonio";
		nombre[5]="Maria";
		nombre[6]="Carlos";
		nombre[7]="Paula";
		nombre[8]="Nicolas";
		nombre[9]="Julia";
		nombre[10]="Roberto";
		nombre[11]="Alba";
		nombre[12]="Daniel";
		return nombre[(int)(Math.random()*13)];
	}

	public static String apellidos() {
		String apellido[]=new String[36];
		apellido[0]="García";
		apellido[1]="Gonzalez";
		apellido[2]="Rodriguez";
		apellido[3]="Fernández";
		apellido[4]="Martinez";
		apellido[5]="Sanchez";
		apellido[6]="Perez";
		apellido[7]="Parra";
		apellido[8]="Jimenez";
		apellido[9]="Ferrer";
		apellido[10]="Pascual";
		apellido[11]="Gómez";
		apellido[12]="Megina";
		return apellido[(int)(Math.random()*13)];
	}

}

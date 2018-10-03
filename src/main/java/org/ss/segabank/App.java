package org.ss.segabank;

import org.ss.segabank.domain.*;
import org.ss.segabank.exception.BalanceNotEnoughException;
import org.ss.segabank.exception.NegOrNullAmountException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class App {
	
	private static final Scanner sc = new Scanner( System.in );
	private static final String PERSISTENCE_UNIT = "sega-bank";
	private static Agency currentAgency;
	private static Account currentAccount;
	
	private static void dspMainMenu() {
		
		int response;
		boolean first = true;
		do {
			if ( !first ) {
				System.out.println( "***********************************************" );
				System.out.println( "* Mauvais choix, merci de recommencer !       *" );
				System.out.println( "***********************************************" );
			}
			System.out.println( "***********************************************" );
			System.out.println( "******************MENU GENERAL*****************" );
			System.out.println( "***********************************************" );
			System.out.println( "*                                             *" );
			System.out.println( "* 1 : Gérer les agences                       *" );
			System.out.println( "* 2 : Gérer les comptes                       *" );
			System.out.println( "* 3 : Quitter l'application                   *" );
			System.out.println( "*                                             *" );
			System.out.println( "***********************************************" );
			System.out.print( "*   Votre choix : " );
			try {
				response = sc.nextInt();
			} catch ( InputMismatchException e ) {
				response = -1;
			} finally {
				sc.nextLine();
			}
			first = false;
		} while ( 1 > response || 3 < response );
		
		switch ( response ) {
			case 1:
				dspAgenciesManagementMenu();
				break;
			case 2:
				dspAccountsManagementMenu();
				break;
			default:
				System.out.println( "* Fermeture de l'application... Au revoir !" );
				System.exit( 0 );
		}
	}
	
	private static void dspAgenciesManagementMenu() {
		int response;
		boolean first = true;
		do {
			if ( !first ) {
				System.out.println( "***********************************************" );
				System.out.println( "* Mauvais choix, merci de recommencer !       *" );
				System.out.println( "***********************************************" );
			}
			System.out.println( "*******************************************************" );
			System.out.println( "****************** GESTION DES AGENCES ****************" );
			System.out.println( "*******************************************************" );
			System.out.println( "*                                                     *" );
			System.out.println( "* 1 : Ajouter une agence                              *" );
			System.out.println( "* 2 : Editer une agence                               *" );
			System.out.println( "* 3 : Supprimer une agence                            *" );
			System.out.println( "* 4 : Lister les agences                              *" );
			System.out.println( "* 5 : Gérer les comptes d'une agence                  *" );
			System.out.println( "* 6 : Retour au menu principal                        *" );
			System.out.println( "*                                                     *" );
			System.out.println( "*******************************************************" );
			System.out.print( "*   Votre choix : " );
			try {
				response = sc.nextInt();
			} catch ( InputMismatchException e ) {
				response = -1;
			} finally {
				sc.nextLine();
			}
			first = false;
		} while ( 1 > response || 6 < response );
		switch ( response ) {
			case 1:
				createAgency();
				dspAgenciesManagementMenu();
				break;
			case 2:
				editAgency();
				dspAgenciesManagementMenu();
				break;
			case 3:
				deleteAgency();
				dspAgenciesManagementMenu();
				break;
			case 4:
				dspAgencies();
				dspAgenciesManagementMenu();
			case 5:
				manageAgencyAccounts();
				dspAgenciesManagementMenu();
				break;
			case 6:
			default:
				dspMainMenu();
		}
	}
	
	private static void dspAccountsManagementMenu() {
		if ( null == currentAgency ) {
			dspAgenciesManagementMenu();
		} else {
			int response;
			boolean first = true;
			do {
				if ( !first ) {
					System.out.println( "***********************************************" );
					System.out.println( "* Mauvais choix; merci de recommencer !       *" );
					System.out.println( "***********************************************" );
				}
				System.out.println( "******************************************************************************" );
				System.out.println( "************ GESTION DES COMPTES DE L'AGENCE : " + currentAgency.getCode() + " *************" );
				System.out.println( "*******************************************************************************" );
				System.out.println( "*                                                                             *" );
				System.out.println( "* 1 : Ajouter un compte                                                       *" );
				System.out.println( "* 2 : Lister les comptes de l'agence                                          *" );
				System.out.println( "* 3 : Gérer un compte spécifique                                              *" );
				System.out.println( "* 4 : Retour au menu \"GESTION DES AGENCES\"                                    *" );
				System.out.println( "*                                                                             *" );
				System.out.println( "*******************************************************************************" );
				System.out.print( "*   Votre choix : " );
				try {
					response = sc.nextInt();
				} catch ( InputMismatchException e ) {
					response = -1;
				} finally {
					sc.nextLine();
				}
				first = false;
			} while ( 1 > response || 4 < response );
			switch ( response ) {
				case 1:
					createAccount();
					dspAccountsManagementMenu();
					break;
				case 2:
					dspCurrentAgencyAccounts();
					dspAccountsManagementMenu();
					break;
				case 3:
					manageAnAccount();
					dspAccountsManagementMenu();
					break;
				case 4:
				default:
					currentAgency = null;
					dspAgenciesManagementMenu();
			}
		}
	}
	
	private static void dspCurrentAccountMenu() {
		if ( null == currentAccount ) {
			dspAccountsManagementMenu();
		} else {
			int response;
			boolean first = true;
			do {
				if ( !first ) {
					System.out.println( "***********************************************" );
					System.out.println( "* Mauvais choix, merci de recommencer !       *" );
					System.out.println( "***********************************************" );
				}
				System.out.println( "*******************************************************************************" );
				System.out.println( "************ GESTION DU COMPTE N°" + currentAccount.getId() + " ************" );
				System.out.println( "*******************************************************************************" );
				System.out.println( "*                                                                             *" );
				System.out.println( "* 1 : Faire une transaction                                                   *" );
				System.out.println( "* 2 : Lister les transactions                                                 *" );
				System.out.println( "* 3 : Supprimer le compte                                                     *" );
				System.out.println( "* 4 : Retour au menu précédent                                                *" );
				System.out.println( "*                                                                             *" );
				System.out.println( "*******************************************************************************" );
				System.out.print( "*   Votre choix : " );
				try {
					response = sc.nextInt();
				} catch ( InputMismatchException e ) {
					response = -1;
				} finally {
					sc.nextLine();
				}
				first = false;
			} while ( 1 > response || 4 < response );
			switch ( response ) {
				case 1:
					makeAccountTransaction();
					dspCurrentAccountMenu();
					break;
				case 2:
					dspCurrentAccountTransactions();
					dspCurrentAccountMenu();
					break;
				case 3:
					deleteCurrentAccount();
					dspCurrentAccountMenu();
					break;
				case 4:
				default:
					currentAccount = null;
					dspAccountsManagementMenu();
			}
		}
	}
	
	public static void main( String... args ) {
		dspMainMenu();
	}
	
	//Agency
	private static void createAgency() {
		
		Agency agency = new Agency();
		System.out.println( "******************************************************" );
		System.out.println( "************** Ajout d'une nouvelle agence ***********" );
		System.out.println( "******************************************************" );
		System.out.println( "* Merci de saisir les informations nécessaires :*" );
		System.out.print( "* Code agence : " );
		agency.setCode( sc.nextLine() );
		System.out.println( "* L'agence sera sise à :" );
		Address address = agency.getAddress();
		System.out.print( "* Numéro de la rue : " );
		address.setNumber( sc.nextLine() );
		System.out.print( "* Nom de la rue : " );
		address.setStreet( sc.nextLine() );
		System.out.print( "* Code postal : " );
		address.setZipCode( sc.nextLine() );
		System.out.print( "* Ville : " );
		address.setCity( sc.nextLine() );
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist( agency );
		em.getTransaction().commit();
		em.close();
		emf.close();
	}
	
	private static void editAgency() {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT );
		EntityManager em = emf.createEntityManager();
		
		TypedQuery<Agency> query = em.createQuery( "FROM Agency", Agency.class );
		List<Agency> agencies = query.getResultList();
		if ( agencies != null && agencies.size() > 0 ) {
			int nbAgencies = agencies.size(), index;
			boolean first = true;
			System.out.println( "******************************************************" );
			System.out.println( "**************** Edition d'une agence ****************" );
			System.out.println( "******************************************************" );
			do {
				if ( !first ) {
					System.out.println( "**********************************************************" );
					System.out.println( "* Mauvais choix - agence inconnue; merci de recommencer ! *" );
					System.out.println( "* Taper -1 pour annuler !                                *" );
					System.out.println( "**********************************************************" );
				}
				System.out.println( "**********************************************************" );
				System.out.println( "******************* Liste des agences  *******************" );
				System.out.println( "**********************************************************" );
				agencies.forEach( System.out::println );
				System.out.println( "**********************************************************" );
				System.out.println( "* Merci de saisir l'index de l'agence à mettre à jour  dans la liste en partant de  0 :" );
				System.out.print( "* Index de l'agence : " );
				try {
					index = sc.nextInt();
				} catch ( InputMismatchException e ) {
					index = -2;
				} finally {
					sc.nextLine();
				}
				first = false;
			} while ( -1 > index || index > nbAgencies - 1 );
			
			if ( -1 < index ) {
				Agency agency = agencies.get( index );
				
				System.out.println( "* Modification de l'agence : " + agency.getCode() + "*" );
				System.out.println( "* Merci de saisir les informations à mettre à jour :*" );
				System.out.println( "* Le code :" );
				String code = sc.nextLine().trim();
				if ( !code.isEmpty() ) {
					agency.setCode( code );
				}
				System.out.println( "* L'adresse :" );
				Address address = agency.getAddress();
				System.out.print( "* Numéro de la rue : " );
				String number = sc.nextLine().trim();
				if ( !number.isEmpty() ) {
					address.setNumber( number );
				}
				System.out.print( "* Nom de la rue : " );
				String street = sc.nextLine().trim();
				if ( !street.isEmpty() ) {
					address.setStreet( street );
				}
				System.out.print( "* Code postal : " );
				String zipCode = sc.nextLine().trim();
				if ( !zipCode.isEmpty() ) {
					address.setZipCode( zipCode );
				}
				System.out.print( "* Ville : " );
				String city = sc.nextLine().trim();
				if ( !city.isEmpty() ) {
					address.setCity( city );
				}
				em.getTransaction().begin();
				em.merge( agency );
				em.getTransaction().commit();
			} else {
				dspAgenciesManagementMenu();
			}
		}
		em.close();
		emf.close();
	}
	
	private static void deleteAgency() {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT );
		EntityManager em = emf.createEntityManager();
		
		TypedQuery<Agency> query = em.createQuery( "FROM Agency", Agency.class );
		List<Agency> agencies = query.getResultList();
		if ( agencies != null && agencies.size() > 0 ) {
			int nbAgencies = agencies.size(), index;
			boolean first = true;
			System.out.println( "******************************************************" );
			System.out.println( "************** Suppression d'une agence **************" );
			System.out.println( "******************************************************" );
			do {
				if ( !first ) {
					System.out.println( "**********************************************************" );
					System.out.println( "* Mauvais choix ; agence inconnue; merci de recommencer ! *" );
					System.out.println( "* Taper -1 pour annuler !                                *" );
					System.out.println( "**********************************************************" );
				}
				System.out.println( "**********************************************************" );
				System.out.println( "******************* Liste des agences  *******************" );
				System.out.println( "**********************************************************" );
				agencies.forEach( System.out::println );
				System.out.println( "**********************************************************" );
				System.out.println( "* Merci de saisir l'index de l'agence à supprimer  dans la liste en partant de  0 :" );
				System.out.print( "* Index de l'agence : " );
				try {
					index = sc.nextInt();
				} catch ( InputMismatchException e ) {
					index = -2;
				} finally {
					sc.nextLine();
				}
				first = false;
			} while ( -1 > index || index > nbAgencies - 1 );
			
			if ( -1 < index ) {
				Agency agency = agencies.get( index );
				
				System.out.println( "* Suppression de l'agence : " + agency.getCode() + "*" );
				System.out.println( "* Etes-vous sur(e) ? O/N " );
				System.out.println( "* Reponse : " );
				String response = sc.nextLine().trim();
				if ( response.equals( "O" ) ) {
					em.getTransaction().begin();
					em.remove( agency );
					em.getTransaction().commit();
				}
			} else {
				dspAgenciesManagementMenu();
			}
		}
		em.close();
		emf.close();
	}
	
	private static void dspAgencies() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT );
		EntityManager em = emf.createEntityManager();
		
		TypedQuery<Agency> query = em.createQuery( "FROM Agency", Agency.class );
		List<Agency> agencies = query.getResultList();
		if ( agencies != null && agencies.size() > 0 ) {
			
			System.out.println( "**********************************************************" );
			System.out.println( "******************* Liste des agences  *******************" );
			System.out.println( "**********************************************************" );
			agencies.forEach( System.out::println );
			System.out.println( "**********************************************************" );
		}
		em.close();
		emf.close();
	}
	
	private static void manageAgencyAccounts() {
		currentAgency = null;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT );
		EntityManager em = emf.createEntityManager();
		
		TypedQuery<Agency> query = em.createQuery( "FROM Agency", Agency.class );
		List<Agency> agencies = query.getResultList();
		if ( agencies != null && agencies.size() > 0 ) {
			int nbAgencies = agencies.size(), index;
			boolean first = true;
			System.out.println( "******************************************************" );
			System.out.println( "********* Gestion des comptes d'une agence ***********" );
			System.out.println( "******************************************************" );
			do {
				if ( !first ) {
					System.out.println( "**********************************************************" );
					System.out.println( "* Mauvais choix - agence inconnue; merci de recommencer ! *" );
					System.out.println( "* Taper -1 pour annuler !                                *" );
					System.out.println( "**********************************************************" );
				}
				System.out.println( "**********************************************************" );
				System.out.println( "******************* Liste des agences ********************" );
				System.out.println( "**********************************************************" );
				agencies.forEach( System.out::println );
				System.out.println( "**********************************************************" );
				System.out.println( "* Merci de saisir l'index de l'agence à gérer  dans la liste en partant de  0 :" );
				System.out.print( "* Index de l'agence : " );
				try {
					index = sc.nextInt();
				} catch ( InputMismatchException e ) {
					index = -2;
				} finally {
					sc.nextLine();
				}
				first = false;
			} while ( -1 > index || index > nbAgencies - 1 );
			
			if ( -1 < index ) {
				currentAgency = agencies.get( index );
				dspAccountsManagementMenu();
			} else {
				dspAgenciesManagementMenu();
			}
		}
		em.close();
		emf.close();
	}
	//End agency
	
	
	//Accounts
	private static void createAccount() {
		if ( null == currentAgency ) {
			dspAgenciesManagementMenu();
		} else {
			System.out.println( "**********************************************************************" );
			System.out.println( "********* Ajout d'un nouveau compte dans l'agence : " + currentAgency.getCode() + " *******" );
			System.out.println( "**********************************************************************" );
			System.out.println( "* Merci de saisir les informations nécessaires :                      " );
			boolean first = true;
			int typeChoice;
			do {
				if ( !first ) {
					System.out.println( "*****************************************************************" );
					System.out.println( "* Mauvais choix - type de compte inconnu; merci de recommencer ! *" );
					System.out.println( "* Taper -1 pour annuler !                                       *" );
					System.out.println( "*****************************************************************" );
				}
				System.out.println( "*****************************************************************" );
				System.out.println( "**************** Sélectionner un type de compte  ****************" );
				System.out.println( "*****************************************************************" );
				System.out.println( "* 1 : Compte simple                                             *" );
				System.out.println( "* 2 : Compte épargne                                            *" );
				System.out.println( "* 3 : Compte payant                                              *" );
				System.out.println( "*****************************************************************" );
				
				try {
					typeChoice = sc.nextInt();
				} catch ( InputMismatchException e ) {
					typeChoice = -2;
				} finally {
					sc.nextLine();
				}
				first = false;
			} while ( -1 > typeChoice || typeChoice > 3 );
			
			if ( -1 < typeChoice ) {
				Account account;
				switch ( typeChoice ) {
					case 1:
						account = new SimpleAccount();
						System.out.print( "* Montant du découvert (0.0 par défaut) : " );
						double overdraft;
						try {
							overdraft = sc.nextDouble();
						} catch ( InputMismatchException e ) {
							overdraft = 0.0;
						} finally {
							sc.nextLine();
						}
						(( SimpleAccount ) account).setOverdraft( overdraft );
						break;
					case 2:
						System.out.print( "* Taux d'interêt (0.0 par défaut) : " );
						account = new SavingAccount();
						double interestRate;
						try {
							interestRate = sc.nextDouble();
						} catch ( InputMismatchException e ) {
							interestRate = 0.0;
						} finally {
							sc.nextLine();
						}
						(( SavingAccount ) account).setInterestRate( interestRate );
						break;
					case 3:
						System.out.print( "* Taux d'arnaque (0.0 par défaut) : " );
						account = new ChargedAccount();
						double chargeRate;
						try {
							chargeRate = sc.nextDouble();
						} catch ( InputMismatchException e ) {
							chargeRate = 0.0;
						} finally {
							sc.nextLine();
						}
						(( ChargedAccount ) account).setChargeRate( chargeRate );
						break;
					default:
						account = null;
				}
				if ( account != null ) {
					System.out.print( "* Versement initial de (0.0 par défaut) : " );
					double amount;
					try {
						amount = sc.nextDouble();
					} catch ( InputMismatchException e ) {
						amount = 0.0;
					} finally {
						sc.nextLine();
					}
					account.initBalance( amount );
					currentAgency.addAccount( account );
					EntityManagerFactory emf = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT );
					EntityManager em = emf.createEntityManager();
					em.getTransaction().begin();
					currentAgency = em.merge( currentAgency );
					em.getTransaction().commit();
					em.close();
					emf.close();
				}
			} else {
				dspAccountsManagementMenu();
			}
		}
	}
	
	private static void deleteCurrentAccount() {
		
		if ( null == currentAccount ) {
			dspAccountsManagementMenu();
		} else {
			System.out.println( "******************************************************" );
			System.out.println( "********** Suppression d'un compte bancaire **********" );
			System.out.println( "******************************************************" );
			System.out.println( "* Suppression du compte N°" + currentAccount.getId() + " - " + currentAccount );
			System.out.println( "* Etes-vous sur(e) ? O/N " );
			System.out.println( "* Reponse : " );
			String response = sc.nextLine().trim();
			if ( response.equals( "O" ) ) {
				EntityManagerFactory emf = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT );
				EntityManager em = emf.createEntityManager();
				em.getTransaction().begin();
				currentAgency.removeAccount( currentAccount );
				currentAgency = em.merge( currentAgency );
				em.getTransaction().commit();
				em.close();
				emf.close();
				currentAccount = null;
			}
		}
	}
	
	private static void dspCurrentAgencyAccounts() {
		if ( null == currentAgency ) {
			dspAgenciesManagementMenu();
		} else {
			System.out.println( "**********************************************************" );
			System.out.println( "******************* Liste des comptes de l'agence : " + currentAgency.getCode() + " ******************" );
			System.out.println( "**********************************************************" );
			currentAgency.getAccounts().forEach( System.out::println );
			System.out.println( "**********************************************************" );
		}
	}
	
	private static void manageAnAccount() {
		
		currentAccount = null;
		if ( null == currentAgency ) {
			dspAgenciesManagementMenu();
		} else {
			Set<Account> accounts = currentAgency.getAccounts();
			if ( accounts != null && accounts.size() > 0 ) {
				
				int nbAccounts = accounts.size(), index;
				boolean first = true;
				System.out.println( "******************************************************" );
				System.out.println( "*********** Gestion d'un compte bancaire *************" );
				System.out.println( "******************************************************" );
				do {
					if ( !first ) {
						System.out.println( "**********************************************************" );
						System.out.println( "* Mauvais choix - compte inconnu; merci de recommencer ! *" );
						System.out.println( "* Taper -1 pour annuler !                                *" );
						System.out.println( "**********************************************************" );
					}
					System.out.println( "**********************************************************" );
					System.out.println( "************* Liste des comptes de l'agence " + currentAgency.getCode() + " **************" );
					System.out.println( "**********************************************************" );
					accounts.forEach( System.out::println );
					System.out.println( "**********************************************************" );
					System.out.println( "* Merci de saisir l'index du compte à gérer  dans la liste en partant de  0 :" );
					System.out.print( "* Index du compte : " );
					try {
						index = sc.nextInt();
					} catch ( InputMismatchException e ) {
						index = -2;
					} finally {
						sc.nextLine();
					}
					first = false;
				} while ( -1 > index || index > nbAccounts );
				
				if ( -1 < index ) {
					int i = 0;
					for ( Account account : accounts ) {
						if ( i == index ) {
							currentAccount = account;
							break;
						}
						i++;
					}
					dspCurrentAccountMenu();
				} else {
					dspAgenciesManagementMenu();
				}
			}
		}
	}
	
	private static void makeAccountTransaction() {
		if ( null == currentAccount ) {
			dspAccountsManagementMenu();
		} else {
			int response, maxChoice;
			boolean first = true;
			do {
				if ( !first ) {
					System.out.println( "***********************************************" );
					System.out.println( "* Mauvais choix, merci de recommencer !       *" );
					System.out.println( "***********************************************" );
				}
				System.out.println( "*******************************************************************************" );
				System.out.println( "********** Opération sur le compte N°" + currentAccount.getId() + " ***********" );
				System.out.println( currentAccount );
				System.out.println( "*******************************************************************************" );
				System.out.println( "*                                                                             *" );
				System.out.println( "* 1 : Faire un versement                                                      *" );
				System.out.println( "* 2 : Fraire un retrait                                                       *" );
				if ( currentAccount instanceof SavingAccount ) {
					System.out.println( "* 3 : Appliquer le taux d'inteêt                                              *" );
					System.out.println( "* 4 : Retour au menu précédent                                                *" );
					maxChoice = 4;
				} else {
					System.out.println( "* 3 : Retour au menu précédent                                                *" );
					maxChoice = 3;
				}
				System.out.println( "*                                                                             *" );
				System.out.println( "*******************************************************************************" );
				System.out.print( "*   Votre choix : " );
				try {
					response = sc.nextInt();
				} catch ( InputMismatchException e ) {
					response = -1;
				} finally {
					sc.nextLine();
				}
				first = false;
			} while ( 1 > response || maxChoice < response );
			double amount = 0.0;
			if ( 1 == response || 2 == response ) {
				try {
					System.out.print( "*   Saisir le montant : " );
					amount = sc.nextDouble();
				} catch ( InputMismatchException e ) {
					amount = 0.0;
				} finally {
					sc.nextLine();
				}
			}
			switch ( response ) {
				case 1:
					try {
						currentAccount.payMoney( amount );
						EntityManagerFactory emf = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT );
						EntityManager em = emf.createEntityManager();
						em.getTransaction().begin();
						currentAccount = em.merge( currentAccount );
						em.getTransaction().commit();
						em.close();
						emf.close();
					} catch ( NegOrNullAmountException e ) {
						System.out.println( e.getMessage() );
					}
					dspCurrentAccountMenu();
					break;
				case 2:
					try {
						currentAccount.drawMoney( amount );
						EntityManagerFactory emf = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT );
						EntityManager em = emf.createEntityManager();
						em.getTransaction().begin();
						currentAccount = em.merge( currentAccount );
						em.getTransaction().commit();
						em.close();
						emf.close();
					} catch ( NegOrNullAmountException e ) {
						System.out.println( e.getMessage() );
					} catch ( BalanceNotEnoughException e ) {
						System.out.println( e.getMessage() );
					}
					dspCurrentAccountMenu();
					break;
				case 3:
					if ( 4 == maxChoice ) {
						(( SavingAccount ) currentAccount).applyInterstRate();
						EntityManagerFactory emf = Persistence.createEntityManagerFactory( PERSISTENCE_UNIT );
						EntityManager em = emf.createEntityManager();
						em.getTransaction().begin();
						currentAccount = em.merge( currentAccount );
						em.getTransaction().commit();
						em.close();
						emf.close();
						dspCurrentAccountMenu();
					} else {
						currentAccount = null;
						dspAccountsManagementMenu();
					}
					break;
				case 4:
				default:
					currentAccount = null;
					dspAccountsManagementMenu();
			}
		}
	}
	
	private static void dspCurrentAccountTransactions() {
		if ( null == currentAccount ) {
			dspAccountsManagementMenu();
		} else {
			System.out.println( "**********************************************************" );
			System.out.println( "*************** Liste des transactions du compte N° " + currentAccount.getId() + "  ****************" );
			System.out.println( currentAccount );
			System.out.println( "**********************************************************" );
			currentAccount.getTransactions().forEach( System.out::println );
			System.out.println( "**********************************************************" );
		}
	}
	//End accounts
}

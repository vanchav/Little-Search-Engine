package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in 
	 * DESCENDING order of frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashSet<String>(100,2.0f);
	}
	
	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
	throws FileNotFoundException {
		//create the hashmap and scan the doc
		HashMap<String,Occurrence> hash = new HashMap<String,Occurrence>();
		Scanner doc = new Scanner(new File (docFile));
		
		String add; //next word to add
		
		//runs till doc has more words
		while (doc.hasNext()) {
			add = getKeyword(doc.next());
			
			if(add == null){
				continue;
			//first time ever 
			}else if (!hash.containsKey(add)) {
				hash.put(add, new Occurrence(docFile, 1));
			//hey its back
			} else if (hash.containsKey(add)) {
				hash.get(add).frequency += 1;
			}
		}
		doc.close();
		return hash;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String,Occurrence> kws) {
		ArrayList <Occurrence> temp; 
		int count = 0; //for the loopy loop loop 
 
		for(String word: kws.keySet()) {
			temp = keywordsIndex.get(word); 
			if(temp == null){
				temp = new ArrayList <Occurrence>();
			}

			temp.add(kws.get(word)); 
			keywordsIndex.put(word, temp); 
			count++; 
		}//end while
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * trailing punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		String key = word.toLowerCase(); //returned by the method
		key = key.trim(); 
		char letter;
		
		//starts at the end of the word and sees if it has a punctuation. 
		//if it ever hits a punctuation, it breaks
		for (int i = key.length()-1; i >= 0; i--) {
			letter = key.charAt(i);
			
			//removes any trailing punctuation
			if(isPunctuation(letter)) {
				key = key.substring(0, i); 
			}else {
				break; 
			}
		}
		
		//returns null if any of the remains letter are a punctuation.
		if(key.length()>0) {
			for(int i = 0; i<key.length(); i++) {
				letter = key.charAt(i); 
				if(!isLetter(letter)) {
					return null; 
				}
			}
		}else {
			//if there is nothing left in the string
			return null; 
		}
		
		//checks for noise words
		if (noiseWords.contains(key)) { 
			return null;
		}
		
		return key;
	}//end method
	
	/**returns true if the char is a punctuation. */ 
	private boolean isPunctuation(char l) {
		if(l == '.' || l == ',' || l == '?' && l == ':' && l == ';' && l == '!') {
			return true; 
		} else {
			return false; 
		}
	}
	
	/**returns true if the char is a letter. */
	private boolean isLetter(char l) {
		if(l == 'a' || l == 'b' || l == 'c' || l == 'd' || l == 'e' || l == 'f' || l == 'h' || l == 'i' || l == 'j' || l == 'k' || l == 'g') {
			return true; 
		} else if(l == 'l' || l == 'm' || l == 'n' || l == 'o' || l == 'p' || l == 'q' || l == 'r' || l == 's' || l == 't' || l == 'u') {
			return true; 
		} else if(l == 'v' || l == 'w' || l == 'x' || l == 'y' || l == 'z') {
			return true; 
		}else {
			return false; 
		}
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion is done by
	 * first finding the correct spot using binary search, then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		/** COMPLETE THIS METHOD **/
		
		if (occs.size() == 1) {
			return null;
		}
		
		ArrayList <Integer> seen = new ArrayList <Integer>();
		
		int pos = 0; //current position
		int l = 0; //low
		int m = 0; //middle
		int h = occs.size()-2; //high

		while (h >= l) {
			m = (h+l)/2;
			seen.add(m);
			//same
			if (occs.get(m).frequency == occs.get(occs.size()-1).frequency) {
				pos = m;
				break;
			//less than
			}else if (occs.get(occs.size()-1).frequency < occs.get(m).frequency) {
				l = m+1;
				pos = m+1;
			//greater than
			}else if (occs.get(occs.size()-1).frequency > occs.get(m).frequency) {
				h = m-1;
				pos = m;
			}
			
		}
		occs.add(pos, occs.remove(occs.size()-1));
		return seen;
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of document frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will take precedence over doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matches at all, result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matches, returns null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		ArrayList<Occurrence> w1 = new ArrayList <Occurrence>(); //first word
		ArrayList<Occurrence> w2 = new ArrayList <Occurrence>(); //second word
		ArrayList <String> fin = new ArrayList<String>(5); //final ans
		
		String key1 = kw1.toLowerCase(); 
		String key2 = kw2.toLowerCase(); 
		int first = 0; 
		int second = 0; 
		int count; 
		 
		boolean one = keywordsIndex.containsValue(key1);
		boolean two = keywordsIndex.containsValue(key2);
		
		
		if(keywordsIndex.get(key1) == null || keywordsIndex.get(key2) == null) {
			//nothing in either
			if (keywordsIndex.get(key1) == null && keywordsIndex.get(key2) == null) {
				return null;
			}
			
			//no word 1
			if (keywordsIndex.get(key1) == null && keywordsIndex.get(key2) != null) {
				w2.addAll(keywordsIndex.get(key2));
				
				for(int i = 0; i<w2.size(); i++) {
					fin.add(w2.get(i).document);
				}
				return fin;
			}
			
			//no word 2
			if (keywordsIndex.get(key1) != null && keywordsIndex.get(key2) == null) {
				w1.addAll(keywordsIndex.get(key1));
				
				for(int i = 0; i<w1.size(); i++) {
					fin.add(w1.get(i).document);
				}
				return fin;
			} 
		} else {
			//both exist
			w1.addAll(keywordsIndex.get(key1));
			w2.addAll(keywordsIndex.get(key2));
			
			//both have size zero
			if (w1.size() == 0 && w2.size() == 0) {
				return null;
			}
			
			//no word 1s
			if (w1.size() == 0) {
				for (int i = 0; i < w2.size(); i++) {
					fin.add(w2.get(i).document);
				}
	 		}
			
			//no word 2s
			if (w2.size() == 0) {
				for (int i = 0; i < w1.size(); i++) {
					fin.add(w1.get(i).document);
				}
	 		}
			
			// both have something
			if(w1.size() > 0 && w2.size() > 0){
 
				while (first < w1.size() || second < w2.size()) {
					if (second == w2.size()) {
						count = first; 
						while(count < w1.size()) {
							if (fin.contains(w1.get(count).document)) {
								continue; 
							}
							fin.add(w1.get(count).document);
							
							if (fin.size() == 5) {
								return fin;
							}
							count++; 
						}
						break;
			 		} else if (first == w1.size()) {
			 			count = second; 
						while (count < w2.size()) {
							if (fin.contains(w2.get(count).document)) {
								continue; 
							}
							fin.add(w2.get(count).document);
							
							if (fin.size() == 5) {
								return fin;
							}
							count++; 
						}
						break;
			 		}
					
					if (fin.contains(w1.get(first).document)) {
						first++;
						continue;
					} else if (fin.contains(w2.get(second).document)) {
						second++;
						continue;
					} else if (w1.get(first).frequency == w2.get(second).frequency) {

						fin.add(w1.get(first).document);
						first++;
						if (fin.size() == 5) {
							return fin;
						}
						
						if (fin.contains(w2.get(second).document)) { 
							second++; 
							continue;
						}else{ 
							if (w1.get(first).frequency == w2.get(second).frequency) {
								fin.add(w1.get(first).document);
								first++;
								if (fin.size() == 5) {
									return fin;
								}
								continue;
							}else {
								fin.add(w2.get(second).document);
								second++;
							}
						}
						
						if (fin.size() == 5) {
							return fin;
						}
						
					}else if (w1.get(first).frequency > w2.get(second).frequency) {
						fin.add(w1.get(first).document);
						first++;
						
						if (fin.size() == 5) {
							return fin;
						}
					}else if (w2.get(second).frequency > w1.get(first).frequency) {
						fin.add(w2.get(second).document);
						first++;
						
						if (fin.size() == 5) {
							return fin;
						}
					}
				}//end of while
			}//size greater than 0
		}//end if either of them exist

		if (w1.size() == 0 && w2.size() == 0) {
			fin = null; 
		}

		return fin;
	
	}//end of top5
}//end of class

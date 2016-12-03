#include "Arduino.h"
#include "aes.h"
//#include "utils.h"

#include <avr/pgmspace.h>
#include <EEPROM.h>

#define Nb 4
#define Nk 8
#define Nr 14

const int S_INV_START_EEPROM = 512;

/*const unsigned char s[256] = 
{
   0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76,
   0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0,
   0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15,
   0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75,
   0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84,
   0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF,
   0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8,
   0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2,
   0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73,
   0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB,
   0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79,
   0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08,
   0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A,
   0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E,
   0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF,
   0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16
};

const unsigned char inv_s[256] = 
{
   0x52, 0x09, 0x6A, 0xD5, 0x30, 0x36, 0xA5, 0x38, 0xBF, 0x40, 0xA3, 0x9E, 0x81, 0xF3, 0xD7, 0xFB,
   0x7C, 0xE3, 0x39, 0x82, 0x9B, 0x2F, 0xFF, 0x87, 0x34, 0x8E, 0x43, 0x44, 0xC4, 0xDE, 0xE9, 0xCB,
   0x54, 0x7B, 0x94, 0x32, 0xA6, 0xC2, 0x23, 0x3D, 0xEE, 0x4C, 0x95, 0x0B, 0x42, 0xFA, 0xC3, 0x4E,
   0x08, 0x2E, 0xA1, 0x66, 0x28, 0xD9, 0x24, 0xB2, 0x76, 0x5B, 0xA2, 0x49, 0x6D, 0x8B, 0xD1, 0x25,
   0x72, 0xF8, 0xF6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xD4, 0xA4, 0x5C, 0xCC, 0x5D, 0x65, 0xB6, 0x92,
   0x6C, 0x70, 0x48, 0x50, 0xFD, 0xED, 0xB9, 0xDA, 0x5E, 0x15, 0x46, 0x57, 0xA7, 0x8D, 0x9D, 0x84,
   0x90, 0xD8, 0xAB, 0x00, 0x8C, 0xBC, 0xD3, 0x0A, 0xF7, 0xE4, 0x58, 0x05, 0xB8, 0xB3, 0x45, 0x06,
   0xD0, 0x2C, 0x1E, 0x8F, 0xCA, 0x3F, 0x0F, 0x02, 0xC1, 0xAF, 0xBD, 0x03, 0x01, 0x13, 0x8A, 0x6B,
   0x3A, 0x91, 0x11, 0x41, 0x4F, 0x67, 0xDC, 0xEA, 0x97, 0xF2, 0xCF, 0xCE, 0xF0, 0xB4, 0xE6, 0x73,
   0x96, 0xAC, 0x74, 0x22, 0xE7, 0xAD, 0x35, 0x85, 0xE2, 0xF9, 0x37, 0xE8, 0x1C, 0x75, 0xDF, 0x6E,
   0x47, 0xF1, 0x1A, 0x71, 0x1D, 0x29, 0xC5, 0x89, 0x6F, 0xB7, 0x62, 0x0E, 0xAA, 0x18, 0xBE, 0x1B,
   0xFC, 0x56, 0x3E, 0x4B, 0xC6, 0xD2, 0x79, 0x20, 0x9A, 0xDB, 0xC0, 0xFE, 0x78, 0xCD, 0x5A, 0xF4,
   0x1F, 0xDD, 0xA8, 0x33, 0x88, 0x07, 0xC7, 0x31, 0xB1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xEC, 0x5F,
   0x60, 0x51, 0x7F, 0xA9, 0x19, 0xB5, 0x4A, 0x0D, 0x2D, 0xE5, 0x7A, 0x9F, 0x93, 0xC9, 0x9C, 0xEF,
   0xA0, 0xE0, 0x3B, 0x4D, 0xAE, 0x2A, 0xF5, 0xB0, 0xC8, 0xEB, 0xBB, 0x3C, 0x83, 0x53, 0x99, 0x61,
   0x17, 0x2B, 0x04, 0x7E, 0xBA, 0x77, 0xD6, 0x26, 0xE1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0C, 0x7D
};*/


void addRoundKey(unsigned char (*in)[4][Nb],const unsigned long *w, const unsigned char round) {
  unsigned char l = round * Nb;
  int j = 0;
  for(j=0; j<Nb; ++j) {
    //Needs to change for the big endian
    char s0 = (*in)[0][j] ^ ((w[l+j] & 0xFF000000) >> 24);
    char s1 = (*in)[1][j] ^ ((w[l+j] & 0x00FF0000) >> 16);
    char s2 = (*in)[2][j] ^ ((w[l+j] & 0x0000FF00) >> 8);
    char s3 = (*in)[3][j] ^ ((w[l+j] & 0x000000FF) >> 0);
    (*in)[0][j] = s0;
    (*in)[1][j] = s1;
    (*in)[2][j] = s2;
    (*in)[3][j] = s3;
  }
  return;
}

void subBytes(unsigned char (*in)[4][Nb]) {
  unsigned i = 0;
  for(i=0; i < 4; ++i) {
    unsigned j = 0;
    for(j=0; j < Nb; ++j) {
      (*in)[i][j] = EEPROM.read( (unsigned char)(*in)[i][j] );
    }
  }
}

void invSubBytes(unsigned char (*in)[4][Nb]) {
  unsigned char i = 0;
  for(i=0; i<4; ++i) {
    unsigned char j = 0;
    for(j=0; j<Nb; ++j) {
      (*in)[i][j] = EEPROM.read( (unsigned char)(*in)[i][j] + S_INV_START_EEPROM );
    }
  }
}

void shiftRow(unsigned char (*in)[4][Nb],const unsigned char row, const unsigned char count) {
  unsigned char i = 0;
  for(i=0; i<count; ++i) {
    unsigned char j = 0;
    for(j=0; j<Nb-1; ++j) {
      unsigned char aux = (*in)[row][j];
      (*in)[row][j] = (*in)[row][j+1];
      (*in)[row][j+1] = aux;
    }
  }
}

void invShiftRow(unsigned char (*in)[4][Nb],const  unsigned char row, const unsigned char count) {
  unsigned char i = 0;
  for(i=0; i<count; ++i) {
    unsigned char j = 0;
    for(j=Nb-1; j>0; --j) {
      char aux = (*in)[row][j];
      (*in)[row][j] = (*in)[row][j-1];
      (*in)[row][j-1] = aux; 
    }
  }
}

void shiftRows(unsigned char (*in)[4][Nb]) {
  shiftRow(in, 1, 1);
  shiftRow(in, 2, 2);
  shiftRow(in, 3, 3);
}

void invShiftRows(unsigned char (*in)[4][Nb]) {
  invShiftRow(in, 1, 1);
  invShiftRow(in, 2, 2);
  invShiftRow(in, 3, 3);
}

unsigned char xtime( char val ) {
  unsigned char needsXOR = ( ( val & 0x80 ) != 0 );
  if ( needsXOR ) return ( val << 1 ^ 0x1B );
  else return val << 1;
}

unsigned char xtimes( unsigned char val, unsigned char times ) {
  switch( times ) {
    case 0x01: return val;
    case 0x02: return xtime(val);
    case 0x03: return val ^ xtime(val);
    case 0x09: return val ^ xtime(xtime(xtime(val)));
    case 0x0b: return xtime(xtime(xtime(val))) ^ xtime(val) ^ val;
    case 0x0d: return xtime(xtime(xtime(val))) ^ xtime(xtime(val)) ^ val;
    case 0x0e: return xtime(xtime(xtime(val))) ^ xtime(xtime(val)) ^ xtime(val);
    case 0x13: return val ^ xtime(val) ^ xtime(xtime(xtime(xtime(val))));
  }
  return 0; 
}

void mixColumns(unsigned char (*in)[4][Nb]) {
  unsigned char j = 0;
  for(j=0; j<Nb; ++j) {
    char s0 = xtimes((*in)[0][j], 2) ^ xtimes((*in)[1][j], 3) ^ xtimes((*in)[2][j], 1) ^ xtimes((*in)[3][j], 1);
    char s1 = xtimes((*in)[0][j], 1) ^ xtimes((*in)[1][j], 2) ^ xtimes((*in)[2][j], 3) ^ xtimes((*in)[3][j], 1);
    char s2 = xtimes((*in)[0][j], 1) ^ xtimes((*in)[1][j], 1) ^ xtimes((*in)[2][j], 2) ^ xtimes((*in)[3][j], 3);
    char s3 = xtimes((*in)[0][j], 3) ^ xtimes((*in)[1][j], 1) ^ xtimes((*in)[2][j], 1) ^ xtimes((*in)[3][j], 2);
    (*in)[0][j] = s0;
    (*in)[1][j] = s1;
    (*in)[2][j] = s2;
    (*in)[3][j] = s3;
  }
}

void invMixColumns(unsigned char (*in)[4][Nb]) {
  unsigned char j = 0;
  for(j=0; j<Nb; ++j) {
    char s0 = xtimes((*in)[0][j], 0x0e) ^ xtimes((*in)[1][j], 0x0b) ^ xtimes((*in)[2][j], 0x0d) ^ xtimes((*in)[3][j], 0x09);
    char s1 = xtimes((*in)[0][j], 0x09) ^ xtimes((*in)[1][j], 0x0e) ^ xtimes((*in)[2][j], 0x0b) ^ xtimes((*in)[3][j], 0x0d);
    char s2 = xtimes((*in)[0][j], 0x0d) ^ xtimes((*in)[1][j], 0x09) ^ xtimes((*in)[2][j], 0x0e) ^ xtimes((*in)[3][j], 0x0b);
    char s3 = xtimes((*in)[0][j], 0x0b) ^ xtimes((*in)[1][j], 0x0d) ^ xtimes((*in)[2][j], 0x09) ^ xtimes((*in)[3][j], 0x0e);
    (*in)[0][j] = s0;
    (*in)[1][j] = s1;
    (*in)[2][j] = s2;
    (*in)[3][j] = s3;
  }
}

void cipher(const unsigned char in[4 * Nb],const unsigned long w[Nb * (Nr+1)], unsigned char *out) {
  unsigned char state[4][Nb];

  // initialize the state
  unsigned char i;
  for(i=0; i<4; ++i) {
    unsigned char j;
    for(j=0; j< Nb; ++j) {
      state[j][i] = in[4*i + j];
    }
  }

  //add first round key
  addRoundKey(&state, w, 0);
  
  //repet the process Nr-1 times
  for(i=1; i<Nr; ++i) {
    subBytes(&state);
    shiftRows(&state);
    mixColumns(&state);
    addRoundKey(&state, w, (unsigned char)i);
  }

  // last iteration
  subBytes(&state);
  shiftRows(&state);
  addRoundKey(&state, w, (unsigned char)Nr);

  // return result
  for(i = 0; i < 4; ++i) {
    int j;
    for(j=0; j < Nb; ++j) {
      out[4*j+i] = state[i][j];
    }
  }
}

void invCipher(const unsigned char in[4 * Nb],const unsigned long w[Nb * (Nr+1)], unsigned char *out) {
  unsigned char state[4][4];

  unsigned char i = 0;
  for(i=0; i<4; ++i) {
          int j = 0;
          for(j=0; j<Nb; ++j) {
                  state[j][i] = in[4*i+j];
          }
  }

  addRoundKey(&state, w, (unsigned char)(Nr));

  for(i=Nr-1; i > 0; --i) {
          invShiftRows(&state);
          invSubBytes(&state);
          addRoundKey(&state, w, (unsigned char)(i));
          invMixColumns(&state);
  }
  invShiftRows(&state);
  invSubBytes(&state);
  addRoundKey(&state, w, 0);

  for(i=0; i<4; ++i) {
          unsigned char j=0;
          for(j=0; j < Nb; ++j) {
                  out[4*i+j] = state[j][i];
          }
  }
}

unsigned long SubWord(unsigned long word) {
  unsigned char byte3 = 0x000000ff & word;
  unsigned char byte2 = ((0x0000ff00 & word) >> 8);
  unsigned char byte1 = ((0x00ff0000 & word) >> 16);
  unsigned char byte0 = ((0xff000000 & word) >> 24);
  byte3 = EEPROM.read(byte3);
  byte2 = EEPROM.read(byte2);
  byte1 = EEPROM.read(byte1);
  byte0 = EEPROM.read(byte0);
  unsigned long rez = ( 
        ((unsigned long)byte0 << 24) | 
        ((unsigned long)byte1 << 16) | 
        ((unsigned long)byte2 << 8) |
        ((unsigned long)byte3) );
  return rez;
}

unsigned long RotWord(unsigned long word) {
  unsigned char byte3 = 0x000000ff & word;
  unsigned char byte2 = ((0x0000ff00 & word) >> 8);
  unsigned char byte1 = ((0x00ff0000 & word) >> 16);
  unsigned char byte0 = ((0xff000000 & word) >> 24);
  unsigned long rez = (
      ((unsigned long)byte1 << 24) |
      ((unsigned long)byte2 << 16) |
      ((unsigned long)byte3 << 8) |
      ((unsigned long)byte0 ) );
  return rez;
}

void keyExpansion(const unsigned char key[4 * Nk], unsigned long w[Nb * (Nr+1)]) {
  unsigned char Rcon[16] =
  {
     0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a 
  };

  unsigned long temp;
  int i = 0;
  while ( i < Nk ) {
    w[i] = ((((unsigned long)(key[4*i])) << 24) |
           (((unsigned long)(key[4*i+1])) << 16) |
           (((unsigned long)(key[4*i+2])) << 8) |
           ((unsigned long)(key[4*i+3])));
    ++i;
  }

  i = Nk;
  while ( i < Nb * ( Nr + 1 ) ) {
    temp = w[i-1];
    if ( i % Nk == 0 ) {
      temp = RotWord(temp);
      temp = SubWord(temp);
      temp = temp ^ ( (unsigned long)Rcon[i/Nk] << 24 );
    } else {
      if (Nk > 6 && i % Nk == 4) {
        temp = SubWord(temp);
      }
    }
    w[i] = w[i-Nk] ^ temp;
    ++i;
  }
}

bool encryptPassword(const unsigned char* password,const unsigned char* key, byte KEY_SIZE, unsigned char* encryptedPassword) {

  if( strlen((char*)password) % KEY_SIZE == 0 ) {
    unsigned long expansionKey[Nb * (Nr + 1)];
    memset(expansionKey, 0, Nb * (Nr + 1) * sizeof(unsigned long));
    keyExpansion(key, expansionKey);
    int steps = strlen((char*)password) / KEY_SIZE;
    int contor = 0;
    while ( contor < steps ) {
      cipher(&password[contor * KEY_SIZE], expansionKey, &encryptedPassword[contor * KEY_SIZE]);
      ++contor;
    }
    return true;
  }
  return false;
}

bool decryptPassword(const unsigned char* encryptedPassword,const unsigned char *key, byte KEY_SIZE, byte l, unsigned char* password) {
   if ( l % KEY_SIZE == 0 ) {
    unsigned long expansionKey[Nb * (Nr + 1)];
    memset(expansionKey, 0, Nb * (Nr + 1) * sizeof(unsigned long));
    keyExpansion(key, expansionKey);
    int steps = l / KEY_SIZE;
    int contor = 0;
    while( contor < steps ) {
      invCipher(&encryptedPassword[contor * KEY_SIZE], expansionKey, &password[contor * KEY_SIZE]);
      ++contor;
    }
    return true;
   }
   return false;
}

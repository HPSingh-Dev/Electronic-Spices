package com.conductor.apni.littleleappwa.utils;

/**
 * Created by Saipro on 25-02-2017.
 */

public class EmailHelper {
//    public static void sendFeedbackEmail(Context context, String feedbackType) {
//        Resources resources = context.getResources();
//        Intent intentEmail = new Intent(Intent.ACTION_SEND);
//        intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{resources.getText(R.string.feedback_support_email)
//                .toString()});
//        intentEmail.putExtra(Intent.EXTRA_SUBJECT, feedbackType);
//        intentEmail.putExtra(Intent.EXTRA_TEXT,
//                (java.io.Serializable) DeviceInfoUtils.getDeviseInfoForFeedback());
//        intentEmail.setType(Cons.TYPE_OF_EMAIL);
//        context.startActivity(Intent.createChooser(intentEmail, resources.getText(
//                R.string.feedback_choose_email_provider)));
//    }
//
//
//    public static void sendInviteNumber(Context context, String[] selectedContacts) {
//        Resources resourcesNumber = context.getResources();
//        Intent intentNumber = new Intent(Intent.ACTION_SEND);
//
//        String toNumbers = "";
//
//        for (String number : selectedContacts) {
//            toNumbers = toNumbers + number + ";";//separating numbers with semicolon
//        }
//        toNumbers = toNumbers.substring(0, toNumbers.length() - 1);
//                /*subString(0, toNumbers.length - 1);*/
//        intentNumber.putExtra("address", toNumbers);
//
//       // intentNumber.putExtra(Intent.EXTRA_TEXT, resourcesNumber.getText(R.string.invite_friends_body_of_invitation));
//        intentNumber.setType(Cons.TYPE_OF_SMS);
//
//        context.startActivity(Intent.createChooser(intentNumber, "Share via"));
//    }

    /*/////////////////////////Contacts List Show/////////////////////////////////////////////////////////////*/
//    public static List<ContactsList> getContactsWithNumber(Context context, SessionManager sessionManager, ContactListDataManager contactListDataManager) {
//        List<ContactsList> friendsContactsList = new ArrayList<ContactsList>();
//        Uri uri = null;
//        ContentResolver contentResolver = context.getContentResolver();
//        /*String[] PROJECTION = new String[]{ContactsContract.RawContacts._ID,
//                ContactsContract.Contacts.DISPLAY_NAME + ") ASC",
//                ContactsContract.Contacts.PHOTO_ID,
//                ContactsContract.CommonDataKinds.Email.DATA,
//                ContactsContract.CommonDataKinds.Photo.CONTACT_ID};
//
//        String order = "upper(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC";
//
//        String filter = ContactsContract.CommonDataKinds.Email.DATA + " NOT LIKE ''";*/
//        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI
//                , null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
//
//        String id;
//        String name;
//        String number;
//        String image;
//        String secLastNumber = "0";
//        String lastNumber = "0";
//        String semiNumber = "";
//        String mobileNumber = "";
//        if (cursor != null) {
//            if (cursor.getCount() > 0) {
//                while (cursor.moveToNext()) {
//                    id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//
//                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
//                        Cursor pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
//                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
//
//                        while (pCur.moveToNext()) {
//                            id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                            name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//
//                            number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            number = number.replaceAll("\\s+", "");
//                            Log.w("Leena", "Cursor size===" + number);
//                            mobileNumber = number;
//                            if(number.startsWith("0")){
//                                mobileNumber=mobileNumber.substring(1);
//                            }
//                            if(number.startsWith("+91")){
//                                mobileNumber=mobileNumber.substring(3);
//                            }
//
//                            if (number.length() >= 10) {
//                                if (number.length() == 10) {
//                                    number = "+91" + number;
//                                } else if (number.length() > 10 && number.startsWith("0")) {
//                                    semiNumber = number.substring(1);
//                                    number = "+91" + semiNumber;
//                                }
//                            }
//
//
//                            image = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
//
//                            if (ContactsContract.Contacts.CONTENT_URI != null) {
//                                uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(
//                                        id));
//                                uri = Uri.withAppendedPath(uri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
//                            }
//                            Log.w("Leena", "Cursor size= list===id====" + id);
//                            Log.w("Leena", "Cursor size= list===uri====" + uri);
//                            Log.w("Leena", "Cursor size= list===id====" + id);
//
//                            //  if (!number.equals(sessionManager.get())) {
//                            if (!lastNumber.equals(number) && !secLastNumber.equals(number) && !number.contains(" ") && number.length() >= 10) {
//                                secLastNumber = lastNumber;
//                                lastNumber = number;
//                                Log.w("Leena", "Cursor size= list==" + number);
//                                ContactsList contactsList=new ContactsList(id, name, null, ContactsList.VIA_CONTACTS_TYPE,
//                                        uri, false, mobileNumber,false);
//                                /*contactListDataManager.createOrUpdate(contactsList);*/
//                                friendsContactsList.add(new ContactsList(id, name, null, ContactsList.VIA_CONTACTS_TYPE,
//                                        uri, false, mobileNumber,false));
//                            }
//                            //   }
//                        }
//                        pCur.close();
//                    }
//                }
//            }
//        }
//        if (cursor != null) {
//            cursor.close();
//        }
//        Log.w("Leena", "list size=====" + friendsContactsList.size());
//        contactListDataManager.createOrUpdateAll(friendsContactsList);
//        return friendsContactsList;
//    }

//    public static void sendContactUsEmail(Context context, String Subject, String mob, String name, String msg) {
//        Resources resources = context.getResources();
//        SessionManager sessionManager=new SessionManager(context);
//        Intent intentEmail = new Intent(Intent.ACTION_SEND);
//        intentEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{resources.getText(R.string.feedback_support_email)
//                .toString()});
//        intentEmail.putExtra(Intent.EXTRA_SUBJECT, Subject);
//        intentEmail.putExtra(Intent.EXTRA_TEXT,
//                (java.io.Serializable) DeviceInfoUtils.getDeviseInfoForFeedback()+"Sales Person Details:\n Name- "+sessionManager.getUserName()+"\nMobile- "+sessionManager.getMobileNo()+"\nPromo Code- "+sessionManager.getPromoCode()+"\nUser Id- "+sessionManager.getUserId()+"\n----------\nCustomer Details:\nName: "+name+" \nMobile: "+mob+"\n----------\nQuery:\n"+msg);
//        intentEmail.setType(Cons.TYPE_OF_EMAIL);
//        context.startActivity(Intent.createChooser(intentEmail, resources.getText(
//                R.string.feedback_choose_email_provider)));
//    }
}

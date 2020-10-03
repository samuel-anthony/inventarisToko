<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class makanan extends Model
{
    protected $primaryKey = 'makanan_id';
    public function pesananDetail(){
        return $this->belongsTo('App\pesananDetail','makanan_id','makanan_id');
    }

    public function makananDetails(){
        return $this->hasMany('App\makananDetail','makanan_id','makanan_id');
    }

    public function jenisMenuDetail(){
        return $this->belongsTo('App\jenisMenuDetail','makanan_id','makanan_id');
    }

    
    public function gambarMakanan(){
        return $this->belongsTo('App\gambarMakanan','makanan_id','makanan_id');
    }
}
